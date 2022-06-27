package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.common.utils.JsonPage;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.repositories.GalaxyRepository;
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

class PlanetIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private GalaxyRepository galaxyRepository;

    @Autowired
    private StarRepository starRepository;

    @Autowired
    private PlanetRepository planetRepository;

    private Star star;
    private PlanetDto planet1;
    private PlanetDto planet2;

    @BeforeEach
    void setup() {
        Galaxy galaxy = galaxyRepository.save(TestUtils.buildGalaxyForAdd());

        star = TestUtils.buildStarForAdd();
        star.setGalaxy(galaxy);
        star = starRepository.save(star);

        planet1 = modelMapper.map(
            planetRepository.save(Planet.builder().name("name1").star(star).build()),
            PlanetDto.class
        );

        planet2 = modelMapper.map(
            planetRepository.save(Planet.builder().name("name2").star(star).build()),
            PlanetDto.class
        );
    }

    @AfterEach
    void cleanup() {
        planetRepository.deleteAllInBatch();
        starRepository.deleteAllInBatch();
        galaxyRepository.deleteAllInBatch();
    }

    @Test
    void testGetList() throws Exception {
        // given
        var nameFilter = "E1";

        // when
        MockHttpServletResponse response = performRequest(get("/planet/get-list")
            .param("name", nameFilter)
        );

        // then
        JsonPage<PlanetDto> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(result.getContent())
            .hasSize(1)
            .hasSameElementsAs(List.of(planet1));
    }

    @Test
    void testGet() throws Exception {
        // given
        UUID id = planet1.getId();

        // when
        MockHttpServletResponse response = performRequest(get("/planet/get/{id}", id));

        // then
        PlanetDto result = readResponse(response, PlanetDto.class);
        assertThat(result).isEqualTo(planet1);
    }

    @Test
    void testAdd() throws Exception {
        // given
        PlanetDto planetDto3 = PlanetDto.builder()
            .name("name3")
            .star(StarDto.builder().id(star.getId()).build())
            .build();

        MockHttpServletResponse response = performRequestWithBody(
            post("/planet/add"),
            planetDto3
        );
        PlanetDto planet3 = readResponse(response, PlanetDto.class);

        // when
        response = performRequest(get("/planet/get-list"));

        // then
        JsonPage<PlanetDto> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(result.getContent())
            .hasSize(3)
            .hasSameElementsAs(List.of(planet1, planet2, planet3))
            .allMatch(item -> item.getStar().getId().equals(star.getId()));

        verifyEventsByType(Map.ofEntries(
            Map.entry(EventType.PLANET_ADD.toString(), 1L)
        ));
    }

    @Test
    void testUpdate() throws Exception {
        // given
        planet1.setName(planet1.getName() + "Update");
        MockHttpServletResponse response = performRequestWithBody(put("/planet/update"), planet1);
        planet1 = readResponse(response, PlanetDto.class);

        // when
        response = performRequest(get("/planet/get-list"));

        // then
        JsonPage<PlanetDto> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(result.getContent())
            .hasSize(2)
            .hasSameElementsAs(List.of(planet1, planet2));

        verifyEventsByType(Map.ofEntries(
            Map.entry(EventType.PLANET_UPDATE.toString(), 1L)
        ));
    }

    @Test
    void testDelete() throws Exception {
        // given
        performRequest(delete("/planet/delete/{id}", planet1.getId()));

        // when
        MockHttpServletResponse response = performRequest(get("/planet/get-list"));

        // then
        JsonPage<PlanetDto> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(result.getContent())
            .hasSize(1)
            .hasSameElementsAs(List.of(planet2));

        verifyEventsByType(Map.ofEntries(
            Map.entry(EventType.PLANET_DELETE.toString(), 1L)
        ));
    }
}
