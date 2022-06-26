package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.common.utils.JsonPage;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.dtos.MoonDto;
import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.types.EventType;
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

class MoonIntegrationTest extends AbstractIntegrationTest {

    @Test
    void test() throws Exception {
        // ----------------------------------------setup----------------------------------------

        // add galaxy
        GalaxyDto galaxyDto = TestUtils.buildGalaxyDtoForAdd();
        MockHttpServletResponse response = performRequestWithBody(post("/galaxy/add"), galaxyDto);
        GalaxyDto addedGalaxy = readResponse(response, GalaxyDto.class);

        // add star
        StarDto starDto = TestUtils.buildStarDtoForAdd();
        starDto.getGalaxy().setId(addedGalaxy.getId());

        response = performRequestWithBody(post("/star/add"), starDto);
        StarDto addedStar = readResponse(response, StarDto.class);

        // add planet
        PlanetDto planetDto = TestUtils.buildPlanetDtoForAdd();
        planetDto.getStar().setId(addedStar.getId());

        response = performRequestWithBody(post("/planet/add"), planetDto);
        PlanetDto addedPlanet = readResponse(response, PlanetDto.class);

        // ----------------------------------------test add----------------------------------------

        // add entity
        MoonDto dto1 = TestUtils.buildMoonDtoForAdd();
        dto1.setName("name1");
        dto1.getPlanet().setId(addedPlanet.getId());

        response = performRequestWithBody(post("/moon/add"), dto1);
        MoonDto addedDto1 = readResponse(response, MoonDto.class);

        // add another entity
        MoonDto dto2 = TestUtils.buildMoonDtoForAdd();
        dto2.setName("name2");
        dto2.getPlanet().setId(addedPlanet.getId());

        response = performRequestWithBody(post("/moon/add"), dto2);
        MoonDto addedDto2 = readResponse(response, MoonDto.class);

        // when
        response = performRequest(get("/moon/get-list"));

        // then
        JsonPage<MoonDto> resultList = readGenericPageResponse(response);
        assertThat(resultList.getContent())
            .isEqualTo(List.of(addedDto1, addedDto2))
            .allMatch(item -> item.getPlanet().getId().equals(addedPlanet.getId()));

        // ----------------------------------------test get----------------------------------------

        // given
        UUID id = addedDto1.getId();

        // when
        response = performRequest(get("/moon/get/{id}", id));

        // then
        MoonDto result = readResponse(response, MoonDto.class);
        assertThat(result).isEqualTo(addedDto1);

        // ----------------------------------------test update----------------------------------------

        // given
        addedDto1.setName(addedDto1.getName() + "Update");
        response = performRequestWithBody(put("/moon/update"), addedDto1);
        MoonDto updatedDto1 = readResponse(response, MoonDto.class);

        id = addedDto1.getId();

        // when
        response = performRequest(get("/moon/get/{id}", id));

        // then
        result = readResponse(response, MoonDto.class);
        assertThat(result).isEqualTo(updatedDto1);

        // ----------------------------------------test getList----------------------------------------

        // given
        var nameFilter = "1uP";

        // when
        response = performRequest(get("/moon/get-list")
            .param("name", nameFilter)
        );

        // then
        resultList = readGenericPageResponse(response);
        assertThat(resultList.getContent()).isEqualTo(List.of(updatedDto1));

        // ----------------------------------------test delete----------------------------------------

        // given
        performRequest(delete("/moon/delete/{id}", addedDto1.getId()));
        performRequest(delete("/moon/delete/{id}", addedDto2.getId()));

        // when
        response = performRequest(get("/moon/get-list"));

        // then
        resultList = readGenericPageResponse(response);
        assertThat(resultList.getContent()).isEmpty();

        // ----------------------------------------test application events----------------------------------------

        verifyEventsByType(Map.ofEntries(
            Map.entry(EventType.GALAXY_ADD.toString(), 1L),
            Map.entry(EventType.STAR_ADD.toString(), 1L),
            Map.entry(EventType.PLANET_ADD.toString(), 1L),
            Map.entry(EventType.MOON_ADD.toString(), 2L),
            Map.entry(EventType.MOON_UPDATE.toString(), 1L),
            Map.entry(EventType.MOON_DELETE.toString(), 2L)
        ));

        // ----------------------------------------cleanup----------------------------------------

        performRequest(delete("/planet/delete/{id}", addedPlanet.getId()));
        performRequest(delete("/star/delete/{id}", addedStar.getId()));
        performRequest(delete("/galaxy/delete/{id}", addedGalaxy.getId()));
    }
}
