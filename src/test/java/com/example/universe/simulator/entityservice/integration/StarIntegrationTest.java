package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.common.utils.JsonPage;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.repositories.GalaxyRepository;
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

class StarIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private GalaxyRepository galaxyRepository;

    @Autowired
    private StarRepository starRepository;

    private Galaxy galaxy;
    private StarDto star1;
    private StarDto star2;

    @BeforeEach
    void setup() {
        galaxy = galaxyRepository.save(TestUtils.buildGalaxyForAdd());

        star1 = modelMapper.map(
            starRepository.save(Star.builder().name("name1").galaxy(galaxy).build()),
            StarDto.class
        );

        star2 = modelMapper.map(
            starRepository.save(Star.builder().name("name2").galaxy(galaxy).build()),
            StarDto.class
        );
    }

    @AfterEach
    void cleanup() {
        starRepository.deleteAllInBatch();
        galaxyRepository.deleteAllInBatch();
    }

    @Test
    void testGetList() throws Exception {
        // given
        var nameFilter = "E1";

        // when
        MockHttpServletResponse response = performRequest(get("/star/get-list")
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
        MockHttpServletResponse response = performRequest(get("/star/get/{id}", id));

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
            post("/star/add"),
            starDto3
        );
        StarDto star3 = readResponse(response, StarDto.class);

        // when
        response = performRequest(get("/star/get-list"));

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
        MockHttpServletResponse response = performRequestWithBody(put("/star/update"), star1);
        star1 = readResponse(response, StarDto.class);

        // when
        response = performRequest(get("/star/get-list"));

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
        performRequest(delete("/star/delete/{id}", star1.getId()));

        // when
        MockHttpServletResponse response = performRequest(get("/star/get-list"));

        // then
        JsonPage<StarDto> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(result.getContent())
            .hasSize(1)
            .hasSameElementsAs(List.of(star2));

        verifyEventsByType(Map.ofEntries(
            Map.entry(EventType.STAR_DELETE.toString(), 1L)
        ));
    }
}
