package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.common.utils.JsonPage;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.mappers.StarMapper;
import com.example.universe.simulator.entityservice.repositories.GalaxyRepository;
import com.example.universe.simulator.entityservice.repositories.StarRepository;
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

import static com.example.universe.simulator.entityservice.services.StarService.CACHE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

class StarIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private GalaxyRepository galaxyRepository;

    @Autowired
    private StarRepository starRepository;

    @Autowired
    private StarMapper mapper;

    private Galaxy galaxy;
    private StarDto star1;
    private StarDto star2;

    @BeforeEach
    void setup() {
        galaxy = galaxyRepository.save(TestUtils.buildGalaxyForAdd());

        star1 = mapper.toDto(starRepository.save(Star.builder().name("name1").galaxy(galaxy).build()));
        star2 = mapper.toDto(starRepository.save(Star.builder().name("name2").galaxy(galaxy).build()));
    }

    @AfterEach
    void cleanup() {
        starRepository.deleteAllInBatch();
        galaxyRepository.deleteAllInBatch();

        Optional.ofNullable(cacheManager.getCache(CACHE_NAME))
            .ifPresent(Cache::clear);
    }

    @Test
    void testGetList() throws Exception {
        // given
        var nameFilter = "E1";

        // when
        MockHttpServletResponse response = performRequest(get("/stars")
            .param("name", nameFilter)
        );

        // then
        JsonPage<StarDto> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(result.getContent())
            .hasSize(1)
            .hasSameElementsAs(List.of(star1));
    }

    @Test
    void testGet() throws Exception {
        // given
        UUID id = star1.getId();

        // when
        MockHttpServletResponse response = performRequest(get("/stars/{id}", id));

        // then
        StarDto result = readResponse(response, StarDto.class);
        assertThat(result).isEqualTo(star1);
    }

    @Test
    void testAdd() throws Exception {
        // given
        StarDto starDto3 = StarDto.builder()
            .name("name3")
            .galaxy(GalaxyDto.builder().id(galaxy.getId()).build())
            .build();

        MockHttpServletResponse response = performRequestWithBody(
            post("/stars"),
            starDto3
        );
        StarDto star3 = readResponse(response, StarDto.class);

        // when
        response = performRequest(get("/stars"));

        // then
        JsonPage<StarDto> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(result.getContent())
            .hasSize(3)
            .hasSameElementsAs(List.of(star1, star2, star3))
            .allMatch(item -> item.getGalaxy().getId().equals(galaxy.getId()));

        verifyEventsByType(Map.ofEntries(
            Map.entry(EventType.STAR_ADD.toString(), 1L)
        ));
    }

    @Test
    void testUpdate() throws Exception {
        // given
        star1.setName(star1.getName() + "Update");
        MockHttpServletResponse response = performRequestWithBody(put("/stars"), star1);
        star1 = readResponse(response, StarDto.class);

        // when
        response = performRequest(get("/stars"));

        // then
        JsonPage<StarDto> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(result.getContent())
            .hasSize(2)
            .hasSameElementsAs(List.of(star1, star2));

        verifyEventsByType(Map.ofEntries(
            Map.entry(EventType.STAR_UPDATE.toString(), 1L)
        ));
    }

    @Test
    void testDelete() throws Exception {
        // given
        performRequest(delete("/stars/{id}", star1.getId()));

        // when
        MockHttpServletResponse response = performRequest(get("/stars"));

        // then
        JsonPage<StarDto> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(result.getContent())
            .hasSize(1)
            .hasSameElementsAs(List.of(star2));

        verifyEventsByType(Map.ofEntries(
            Map.entry(EventType.STAR_DELETE.toString(), 1L)
        ));
    }

    @Test
    void testCaching_add() throws Exception {
        // given
        UUID id = star1.getId();

        // when
        performRequest(get("/stars/{id}", id));

        // then
        Optional<StarDto> cache = Optional.ofNullable(cacheManager.getCache(CACHE_NAME))
            .map(item -> item.get(id, Star.class))
            .map(mapper::toDto);
        assertThat(cache)
            .hasValue(star1);
    }

    @Test
    void testCaching_update_keyNotInCache() throws Exception {
        // when
        performRequestWithBody(put("/stars"), star1);

        // then
        Optional<Star> cache = Optional.ofNullable(cacheManager.getCache(CACHE_NAME))
            .map(item -> item.get(star1.getId(), Star.class));
        assertThat(cache).isEmpty();
    }

    @Test
    void testCaching_update_keyInCache() throws Exception {
        // given

        // cache entity
        performRequest(get("/stars/{id}", star1.getId()));

        // when
        star1.setName(star1.getName() + "Update");
        MockHttpServletResponse response = performRequestWithBody(put("/stars"), star1);
        star1 = readResponse(response, StarDto.class);

        // then
        Optional<StarDto> cache = Optional.ofNullable(cacheManager.getCache(CACHE_NAME))
            .map(item -> item.get(star1.getId(), Star.class))
            .map(mapper::toDto);
        assertThat(cache)
            .hasValue(star1);
    }

    @Test
    void testCaching_delete() throws Exception {
        // given

        UUID id = star1.getId();
        // cache entity
        performRequest(get("/stars/{id}", id));

        // when
        performRequest(delete("/stars/{id}", id));

        // then
        Optional<Star> cache = Optional.ofNullable(cacheManager.getCache(CACHE_NAME))
            .map(item -> item.get(id, Star.class));
        assertThat(cache).isEmpty();
    }
}
