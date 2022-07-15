package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.common.utils.JsonPage;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.dtos.MoonDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.entities.Moon;
import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.mappers.MoonMapper;
import com.example.universe.simulator.entityservice.repositories.GalaxyRepository;
import com.example.universe.simulator.entityservice.repositories.MoonRepository;
import com.example.universe.simulator.entityservice.repositories.PlanetRepository;
import com.example.universe.simulator.entityservice.repositories.StarRepository;
import com.example.universe.simulator.entityservice.services.MoonService;
import com.example.universe.simulator.entityservice.types.EventType;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.example.universe.simulator.entityservice.services.MoonService.CACHE_NAME;
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

    @Autowired
    private MoonMapper mapper;

    private Planet planet;
    private MoonDto moon1;
    private MoonDto moon2;

    @BeforeEach
    void setup() {
        Galaxy galaxy = galaxyRepository.save(TestUtils.buildGalaxyForAdd());

        Star star = TestUtils.buildStarForAdd();
        star.setGalaxyId(galaxy.getId());
        star = starRepository.save(star);

        planet = TestUtils.buildPlanetForAdd();
        planet.setStarId(star.getId());
        planet = planetRepository.save(planet);

        moon1 = mapper.toDto(moonRepository.save(Moon.builder().name("name1").planetId(planet.getId()).build()));
        moon2 = mapper.toDto(moonRepository.save(Moon.builder().name("name2").planetId(planet.getId()).build()));
    }

    @AfterEach
    void cleanup() {
        moonRepository.deleteAllInBatch();
        planetRepository.deleteAllInBatch();
        starRepository.deleteAllInBatch();
        galaxyRepository.deleteAllInBatch();

        Optional.ofNullable(cacheManager.getCache(MoonService.CACHE_NAME))
            .ifPresent(Cache::clear);
    }

    @Test
    void testGetMoons() throws Exception {
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
    void testGetMoon() throws Exception {
        // given
        UUID id = moon1.getId();

        // when
        MockHttpServletResponse response = performRequest(get("/moons/{id}", id));

        // then
        MoonDto result = readResponse(response, MoonDto.class);
        assertThat(result).isEqualTo(moon1);
    }

    @Test
    void testAddMoon() throws Exception {
        // given
        MoonDto moonDto3 = MoonDto.builder()
            .name("name3")
            .planetId(planet.getId())
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
            .hasSameElementsAs(List.of(moon1, moon2, moon3));

        verifyEventsByType(Map.ofEntries(
            Map.entry(EventType.MOON_ADD.toString(), 1L)
        ));
    }

    @Test
    void testUpdateMoon() throws Exception {
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
    void testDeleteMoon() throws Exception {
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

    @Test
    void testCaching_add() throws Exception {
        // given
        UUID id = moon1.getId();

        // when
        performRequest(get("/moons/{id}", id));

        // then
        Optional<MoonDto> cache = Optional.ofNullable(cacheManager.getCache(CACHE_NAME))
            .map(item -> item.get(id, Moon.class))
            .map(mapper::toDto);
        assertThat(cache)
            .hasValue(moon1);
    }

    @Test
    void testCaching_update_keyNotInCache() throws Exception {
        // when
        performRequestWithBody(put("/moons"), moon1);

        // then
        Optional<Moon> cache = Optional.ofNullable(cacheManager.getCache(CACHE_NAME))
            .map(item -> item.get(moon1.getId(), Moon.class));
        assertThat(cache).isEmpty();
    }

    @Test
    void testCaching_update_keyInCache() throws Exception {
        // given

        // cache entity
        performRequest(get("/moons/{id}", moon1.getId()));

        // when
        moon1.setName(moon1.getName() + "Update");
        MockHttpServletResponse response = performRequestWithBody(put("/moons"), moon1);
        moon1 = readResponse(response, MoonDto.class);

        // then
        Optional<MoonDto> cache = Optional.ofNullable(cacheManager.getCache(CACHE_NAME))
            .map(item -> item.get(moon1.getId(), Moon.class))
            .map(mapper::toDto);
        assertThat(cache)
            .hasValue(moon1);
    }

    @Test
    void testCaching_delete() throws Exception {
        // given

        UUID id = moon1.getId();
        // cache entity
        performRequest(get("/moons/{id}", id));

        // when
        performRequest(delete("/moons/{id}", id));

        // then
        Optional<Moon> cache = Optional.ofNullable(cacheManager.getCache(CACHE_NAME))
            .map(item -> item.get(id, Moon.class));
        assertThat(cache).isEmpty();
    }
}
