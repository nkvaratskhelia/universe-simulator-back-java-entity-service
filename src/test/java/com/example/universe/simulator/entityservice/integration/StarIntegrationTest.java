package com.example.universe.simulator.entityservice.integration;

import static com.example.universe.simulator.entityservice.services.StarService.CACHE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.example.universe.simulator.entityservice.common.utils.JsonPage;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.inputs.AddStarInput;
import com.example.universe.simulator.entityservice.inputs.UpdateStarInput;
import com.example.universe.simulator.entityservice.mappers.StarMapper;
import com.example.universe.simulator.entityservice.repositories.GalaxyRepository;
import com.example.universe.simulator.entityservice.repositories.StarRepository;
import com.example.universe.simulator.entityservice.types.EventType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.mock.web.MockHttpServletResponse;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

        star1 = mapper.toDto(starRepository.save(Star.builder().name("name1").galaxyId(galaxy.getId()).build()));
        star2 = mapper.toDto(starRepository.save(Star.builder().name("name2").galaxyId(galaxy.getId()).build()));
    }

    @AfterEach
    void cleanup() {
        starRepository.deleteAllInBatch();
        galaxyRepository.deleteAllInBatch();

        Optional.ofNullable(cacheManager.getCache(CACHE_NAME))
            .ifPresent(Cache::clear);
    }

    @Test
    void testGetStars() throws Exception {
        // given
        var nameFilter = "E1";

        // when
        MockHttpServletResponse response = performRequest(get("/stars")
            .param("name", nameFilter)
        );

        // then
        JsonPage<StarDto> result = jsonMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(result.getContent())
            .hasSize(1)
            .hasSameElementsAs(List.of(star1));
    }

    @Test
    void testGetStar() throws Exception {
        // given
        UUID id = star1.getId();

        // when
        MockHttpServletResponse response = performRequest(get("/stars/{id}", id));

        // then
        StarDto result = readResponse(response, StarDto.class);
        assertThat(result).isEqualTo(star1);
    }

    @Test
    void testAddStar() throws Exception {
        // given
        MockHttpServletResponse response = performRequestWithBody(post("/stars"),
            new AddStarInput("name3", galaxy.getId())
        );
        StarDto star3 = readResponse(response, StarDto.class);

        // when
        response = performRequest(get("/stars"));

        // then
        JsonPage<StarDto> result = jsonMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(result.getContent())
            .hasSize(3)
            .hasSameElementsAs(List.of(star1, star2, star3));

        verifyEventsByType(Map.ofEntries(
            Map.entry(EventType.STAR_ADD.toString(), 1L)
        ));
    }

    @Test
    void testUpdateStar() throws Exception {
        // given
        MockHttpServletResponse response = performRequestWithBody(put("/stars"),
            new UpdateStarInput(star1.getId(), star1.getVersion(), star1.getName() + "Update", star1.getGalaxyId())
        );
        StarDto dto = readResponse(response, StarDto.class);

        // when
        response = performRequest(get("/stars"));

        // then
        JsonPage<StarDto> result = jsonMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(result.getContent())
            .hasSize(2)
            .hasSameElementsAs(List.of(dto, star2));

        verifyEventsByType(Map.ofEntries(
            Map.entry(EventType.STAR_UPDATE.toString(), 1L)
        ));
    }

    @Test
    void testDeleteStar() throws Exception {
        // given
        performRequest(delete("/stars/{id}", star1.getId()));

        // when
        MockHttpServletResponse response = performRequest(get("/stars"));

        // then
        JsonPage<StarDto> result = jsonMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
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
        performRequestWithBody(put("/stars"),
            new UpdateStarInput(star1.getId(), star1.getVersion(), star1.getName() + "Update", star1.getGalaxyId())
        );

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
        MockHttpServletResponse response = performRequestWithBody(put("/stars"),
            new UpdateStarInput(star1.getId(), star1.getVersion(), star1.getName() + "Update", star1.getGalaxyId())
        );
        StarDto dto = readResponse(response, StarDto.class);

        // then
        Optional<StarDto> cache = Optional.ofNullable(cacheManager.getCache(CACHE_NAME))
            .map(item -> item.get(star1.getId(), Star.class))
            .map(mapper::toDto);
        assertThat(cache).hasValue(dto);
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
