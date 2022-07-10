package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.common.utils.JsonPage;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.mappers.GalaxyMapper;
import com.example.universe.simulator.entityservice.repositories.GalaxyRepository;
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

import static com.example.universe.simulator.entityservice.services.GalaxyService.CACHE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

class GalaxyIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private GalaxyRepository galaxyRepository;

    @Autowired
    private GalaxyMapper mapper;

    private GalaxyDto galaxy1;
    private GalaxyDto galaxy2;

    @BeforeEach
    void setup() {
        galaxy1 = mapper.toDto(galaxyRepository.save(Galaxy.builder().name("name1").build()));
        galaxy2 = mapper.toDto(galaxyRepository.save(Galaxy.builder().name("name2").build()));
    }

    @AfterEach
    void cleanup() {
        galaxyRepository.deleteAllInBatch();

        Optional.ofNullable(cacheManager.getCache(CACHE_NAME))
            .ifPresent(Cache::clear);
    }

    @Test
    void testGetList() throws Exception {
        // given
        var nameFilter = "E1";

        // when
        MockHttpServletResponse response = performRequest(get("/galaxies")
            .param("name", nameFilter)
        );

        // then
        JsonPage<GalaxyDto> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(result.getContent())
            .hasSize(1)
            .hasSameElementsAs(List.of(galaxy1));
    }

    @Test
    void testGet() throws Exception {
        // given
        UUID id = galaxy1.getId();

        // when
        MockHttpServletResponse response = performRequest(get("/galaxies/{id}", id));

        // then
        GalaxyDto result = readResponse(response, GalaxyDto.class);
        assertThat(result).isEqualTo(galaxy1);
    }

    @Test
    void testAdd() throws Exception {
        // given
        MockHttpServletResponse response = performRequestWithBody(
            post("/galaxies"),
            GalaxyDto.builder().name("name3").build()
        );
        GalaxyDto galaxy3 = readResponse(response, GalaxyDto.class);

        // when
        response = performRequest(get("/galaxies"));

        // then
        JsonPage<GalaxyDto> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(result.getContent())
            .hasSize(3)
            .hasSameElementsAs(List.of(galaxy1, galaxy2, galaxy3));

        verifyEventsByType(Map.ofEntries(
            Map.entry(EventType.GALAXY_ADD.toString(), 1L)
        ));
    }

    @Test
    void testUpdate() throws Exception {
        // given
        galaxy1.setName(galaxy1.getName() + "Update");
        MockHttpServletResponse response = performRequestWithBody(put("/galaxies"), galaxy1);
        galaxy1 = readResponse(response, GalaxyDto.class);

        // when
        response = performRequest(get("/galaxies"));

        // then
        JsonPage<GalaxyDto> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(result.getContent())
            .hasSize(2)
            .hasSameElementsAs(List.of(galaxy1, galaxy2));

        verifyEventsByType(Map.ofEntries(
            Map.entry(EventType.GALAXY_UPDATE.toString(), 1L)
        ));
    }

    @Test
    void testDelete() throws Exception {
        // given
        performRequest(delete("/galaxies/{id}", galaxy1.getId()));

        // when
        MockHttpServletResponse response = performRequest(get("/galaxies"));

        // then
        JsonPage<GalaxyDto> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(result.getContent())
            .hasSize(1)
            .hasSameElementsAs(List.of(galaxy2));

        verifyEventsByType(Map.ofEntries(
            Map.entry(EventType.GALAXY_DELETE.toString(), 1L)
        ));
    }

    @Test
    void testCaching_add() throws Exception {
        // given
        UUID id = galaxy1.getId();

        // when
        performRequest(get("/galaxies/{id}", id));

        // then
        Optional<GalaxyDto> cache = Optional.ofNullable(cacheManager.getCache(CACHE_NAME))
            .map(item -> item.get(id, Galaxy.class))
            .map(mapper::toDto);
        assertThat(cache)
            .hasValue(galaxy1);
    }

    @Test
    void testCaching_update_keyNotInCache() throws Exception {
        // when
        performRequestWithBody(put("/galaxies"), galaxy1);

        // then
        Optional<Galaxy> cache = Optional.ofNullable(cacheManager.getCache(CACHE_NAME))
            .map(item -> item.get(galaxy1.getId(), Galaxy.class));
        assertThat(cache).isEmpty();
    }

    @Test
    void testCaching_update_keyInCache() throws Exception {
        // given

        // cache entity
        performRequest(get("/galaxies/{id}", galaxy1.getId()));

        // when
        galaxy1.setName(galaxy1.getName() + "Update");
        MockHttpServletResponse response = performRequestWithBody(put("/galaxies"), galaxy1);
        galaxy1 = readResponse(response, GalaxyDto.class);

        // then
        Optional<GalaxyDto> cache = Optional.ofNullable(cacheManager.getCache(CACHE_NAME))
            .map(item -> item.get(galaxy1.getId(), Galaxy.class))
            .map(mapper::toDto);
        assertThat(cache)
            .hasValue(galaxy1);
    }

    @Test
    void testCaching_delete() throws Exception {
        // given

        UUID id = galaxy1.getId();
        // cache entity
        performRequest(get("/galaxies/{id}", id));

        // when
        performRequest(delete("/galaxies/{id}", id));

        // then
        Optional<Galaxy> cache = Optional.ofNullable(cacheManager.getCache(CACHE_NAME))
            .map(item -> item.get(id, Galaxy.class));
        assertThat(cache).isEmpty();
    }
}
