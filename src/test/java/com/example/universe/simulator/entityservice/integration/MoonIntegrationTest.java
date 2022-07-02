package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.common.utils.JsonPage;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.dtos.MoonDto;
import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.entities.Moon;
import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.repositories.GalaxyRepository;
import com.example.universe.simulator.entityservice.repositories.MoonRepository;
import com.example.universe.simulator.entityservice.repositories.PlanetRepository;
import com.example.universe.simulator.entityservice.repositories.StarRepository;
import com.example.universe.simulator.entityservice.types.EventType;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

class MoonIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private GalaxyRepository galaxyRepository;

    @Autowired
    private StarRepository starRepository;

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private MoonRepository moonRepository;

    private Planet planet;
    private MoonDto moon1;
    private MoonDto moon2;

    @BeforeEach
    void setup() {
        Galaxy galaxy = galaxyRepository.save(TestUtils.buildGalaxyForAdd());

        Star star = TestUtils.buildStarForAdd();
        star.setGalaxy(galaxy);
        star = starRepository.save(star);

        planet = TestUtils.buildPlanetForAdd();
        planet.setStar(star);
        planet = planetRepository.save(planet);

        moon1 = modelMapper.map(
            moonRepository.save(Moon.builder().name("name1").planet(planet).build()),
            MoonDto.class
        );

        moon2 = modelMapper.map(
            moonRepository.save(Moon.builder().name("name2").planet(planet).build()),
            MoonDto.class
        );
    }

    @AfterEach
    void cleanup() {
        moonRepository.deleteAllInBatch();
        planetRepository.deleteAllInBatch();
        starRepository.deleteAllInBatch();
        galaxyRepository.deleteAllInBatch();
    }

    @Test
    void testGetList() throws Exception {
        // given
        var nameFilter = "E1";

        // when
        MockHttpServletResponse response = performRequest(get("/moons")
            .param("name", nameFilter)
        );

        // then
        JsonPage<MoonDto> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(result.getContent())
            .hasSize(1)
            .hasSameElementsAs(List.of(moon1));
    }

    @Test
    void testGet() throws Exception {
        // given
        UUID id = moon1.getId();

        // when
        MockHttpServletResponse response = performRequest(get("/moons/{id}", id));

        // then
        MoonDto result = readResponse(response, MoonDto.class);
        assertThat(result).isEqualTo(moon1);
    }

    @Test
    void testAdd() throws Exception {
        // given
        MoonDto moonDto3 = MoonDto.builder()
            .name("name3")
            .planet(PlanetDto.builder().id(planet.getId()).build())
            .build();

        MockHttpServletResponse response = performRequestWithBody(
            post("/moons"),
            moonDto3
        );
        MoonDto moon3 = readResponse(response, MoonDto.class);

        // when
        response = performRequest(get("/moons"));

        // then
        JsonPage<MoonDto> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(result.getContent())
            .hasSize(3)
            .hasSameElementsAs(List.of(moon1, moon2, moon3))
            .allMatch(item -> item.getPlanet().getId().equals(planet.getId()));

        verifyEventsByType(Map.ofEntries(
            Map.entry(EventType.MOON_ADD.toString(), 1L)
        ));
    }

    @Test
    void testUpdate() throws Exception {
        // given
        moon1.setName(moon1.getName() + "Update");
        MockHttpServletResponse response = performRequestWithBody(put("/moons"), moon1);
        moon1 = readResponse(response, MoonDto.class);

        // when
        response = performRequest(get("/moons"));

        // then
        JsonPage<MoonDto> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(result.getContent())
            .hasSize(2)
            .hasSameElementsAs(List.of(moon1, moon2));

        verifyEventsByType(Map.ofEntries(
            Map.entry(EventType.MOON_UPDATE.toString(), 1L)
        ));
    }

    @Test
    void testDelete() throws Exception {
        // given
        performRequest(delete("/moons/{id}", moon1.getId()));

        // when
        MockHttpServletResponse response = performRequest(get("/moons"));

        // then
        JsonPage<MoonDto> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(result.getContent())
            .hasSize(1)
            .hasSameElementsAs(List.of(moon2));

        verifyEventsByType(Map.ofEntries(
            Map.entry(EventType.MOON_DELETE.toString(), 1L)
        ));
    }
}
