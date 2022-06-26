package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.common.utils.JsonPage;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.types.EventType;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
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

    @Test
    void test() throws Exception {
        // ----------------------------------------setup----------------------------------------

        // add galaxy
        GalaxyDto galaxyDto = TestUtils.buildGalaxyDtoForAdd();
        MockHttpServletResponse response = performRequestWithBody(post("/galaxy/add"), galaxyDto);
        GalaxyDto addedGalaxy = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);

        // add star
        StarDto starDto = TestUtils.buildStarDtoForAdd();
        starDto.getGalaxy().setId(addedGalaxy.getId());

        response = performRequestWithBody(post("/star/add"), starDto);
        StarDto addedStar = objectMapper.readValue(response.getContentAsString(), StarDto.class);

        // ----------------------------------------test add----------------------------------------

        // add entity
        PlanetDto dto1 = TestUtils.buildPlanetDtoForAdd();
        dto1.setName("name1");
        dto1.getStar().setId(addedStar.getId());

        response = performRequestWithBody(post("/planet/add"), dto1);
        PlanetDto addedDto1 = objectMapper.readValue(response.getContentAsString(), PlanetDto.class);

        // add another entity
        PlanetDto dto2 = TestUtils.buildPlanetDtoForAdd();
        dto2.setName("name2");
        dto2.getStar().setId(addedStar.getId());

        response = performRequestWithBody(post("/planet/add"), dto2);
        PlanetDto addedDto2 = objectMapper.readValue(response.getContentAsString(), PlanetDto.class);

        // when
        response = performRequest(get("/planet/get-list"));

        // then
        JsonPage<PlanetDto> resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent())
            .isEqualTo(List.of(addedDto1, addedDto2))
            .allMatch(item -> item.getStar().getId().equals(addedStar.getId()));

        // ----------------------------------------test get----------------------------------------

        // given
        UUID id = addedDto1.getId();

        // when
        response = performRequest(get("/planet/get/{id}", id));

        // then
        PlanetDto result = objectMapper.readValue(response.getContentAsString(), PlanetDto.class);
        assertThat(result).isEqualTo(addedDto1);

        // ----------------------------------------test update----------------------------------------

        // given
        addedDto1.setName(addedDto1.getName() + "Update");
        response = performRequestWithBody(put("/planet/update"), addedDto1);
        PlanetDto updatedDto1 = objectMapper.readValue(response.getContentAsString(), PlanetDto.class);

        id = addedDto1.getId();

        // when
        response = performRequest(get("/planet/get/{id}", id));

        // then
        result = objectMapper.readValue(response.getContentAsString(), PlanetDto.class);
        assertThat(result).isEqualTo(updatedDto1);

        // ----------------------------------------test getList----------------------------------------

        // given
        var nameFilter = "1uP";

        // when
        response = performRequest(get("/planet/get-list")
            .param("name", nameFilter)
        );

        // then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).isEqualTo(List.of(updatedDto1));

        // ----------------------------------------test delete----------------------------------------

        // given
        performRequest(delete("/planet/delete/{id}", addedDto1.getId()));
        performRequest(delete("/planet/delete/{id}", addedDto2.getId()));

        // when
        response = performRequest(get("/planet/get-list"));

        // then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).isEmpty();

        // ----------------------------------------test application events----------------------------------------

        verifyEventsByType(Map.ofEntries(
            Map.entry(EventType.GALAXY_ADD.toString(), 1L),
            Map.entry(EventType.STAR_ADD.toString(), 1L),
            Map.entry(EventType.PLANET_ADD.toString(), 2L),
            Map.entry(EventType.PLANET_UPDATE.toString(), 1L),
            Map.entry(EventType.PLANET_DELETE.toString(), 2L)
        ));

        // ----------------------------------------cleanup----------------------------------------

        performRequest(delete("/star/delete/{id}", addedStar.getId()));
        performRequest(delete("/galaxy/delete/{id}", addedGalaxy.getId()));
    }
}
