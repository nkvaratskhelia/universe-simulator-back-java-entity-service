package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.common.utils.JsonPage;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.repositories.GalaxyRepository;
import com.example.universe.simulator.entityservice.services.GalaxyService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

class GalaxyIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private GalaxyRepository galaxyRepository;

    private GalaxyDto galaxy1;
    private GalaxyDto galaxy2;

    @BeforeEach
    void setup() {
        galaxy1 = modelMapper.map(
            galaxyRepository.save(Galaxy.builder().name("name1").build()),
            GalaxyDto.class
        );

        galaxy2 = modelMapper.map(
            galaxyRepository.save(Galaxy.builder().name("name2").build()),
            GalaxyDto.class
        );
    }

    @AfterEach
    void cleanup() {
        galaxyRepository.deleteAllInBatch();
        Optional.ofNullable(cacheManager.getCache(GalaxyService.GALAXY_CACHE_NAME))
                .ifPresent(Cache::clear);
    }

    @Test
    void testGetList() throws Exception {
        // given
        var nameFilter = "E1";

        // when
        MockHttpServletResponse response = performRequest(get("/galaxy/get-list")
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
        MockHttpServletResponse response = performRequest(get("/galaxy/get/{id}", id));

        // then
        GalaxyDto result = readResponse(response, GalaxyDto.class);
        assertThat(result).isEqualTo(galaxy1);
    }

    @Test
    void testAdd() throws Exception {
        // given
        MockHttpServletResponse response = performRequestWithBody(
            post("/galaxy/add"),
            GalaxyDto.builder().name("name3").build()
        );
        GalaxyDto galaxy3 = readResponse(response, GalaxyDto.class);

        // when
        response = performRequest(get("/galaxy/get-list"));

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
        MockHttpServletResponse response = performRequestWithBody(put("/galaxy/update"), galaxy1);
        galaxy1 = readResponse(response, GalaxyDto.class);

        // when
        response = performRequest(get("/galaxy/get-list"));

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
        performRequest(delete("/galaxy/delete/{id}", galaxy1.getId()));

        // when
        MockHttpServletResponse response = performRequest(get("/galaxy/get-list"));

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
    void testCaching() throws Exception {
        MockHttpServletResponse response;
        Optional<GalaxyDto> fromCache;

        // should not update entity in cache, unless it exists there

        // given
        galaxy1.setName(galaxy1.getName() + "Update");
        response = performRequestWithBody(put("/galaxy/update"), galaxy1);
        galaxy1 = readResponse(response, GalaxyDto.class);

        // when
        fromCache = Optional.ofNullable(cacheManager.getCache(GalaxyService.GALAXY_CACHE_NAME))
            .map(item -> item.get(galaxy1.getId(), Galaxy.class))
            .map(item -> modelMapper.map(item, GalaxyDto.class));

        // then
        assertThat(fromCache).isEmpty();

        // should put entity in cache

        // given
        response = performRequest(get("/galaxy/get/{id}", galaxy1.getId()));

        // when
        GalaxyDto result = readResponse(response, GalaxyDto.class);
        fromCache = Optional.ofNullable(cacheManager.getCache(GalaxyService.GALAXY_CACHE_NAME))
            .map(item -> item.get(result.getId(), Galaxy.class))
            .map(item -> modelMapper.map(item, GalaxyDto.class));

        //then
        assertThat(fromCache)
            .isNotEmpty()
            .hasValue(result);

        // should update entity in cache

        // given
        galaxy1 = result;
        galaxy1.setName(galaxy1.getName() + "Update2");
        response = performRequestWithBody(put("/galaxy/update"), galaxy1);
        galaxy1 = readResponse(response, GalaxyDto.class);

        // when
        fromCache = Optional.ofNullable(cacheManager.getCache(GalaxyService.GALAXY_CACHE_NAME))
            .map(item -> item.get(galaxy1.getId(), Galaxy.class))
            .map(item -> modelMapper.map(item, GalaxyDto.class));

        // then
        assertThat(fromCache)
            .isNotEmpty()
            .hasValue(galaxy1);

        // should delete entity in cache

        // given
        performRequest(delete("/galaxy/delete/{id}", galaxy1.getId()));

        // when
        fromCache = Optional.ofNullable(cacheManager.getCache(GalaxyService.GALAXY_CACHE_NAME))
           .map(item -> item.get(galaxy1.getId(), Galaxy.class))
           .map(item -> modelMapper.map(item, GalaxyDto.class));

        // then
        assertThat(fromCache).isEmpty();
    }
}
