package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.common.utils.JsonPage;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
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

class StarIntegrationTest extends AbstractIntegrationTest {

    @Test
    void test() throws Exception {
        // ----------------------------------------setup----------------------------------------

        // add galaxy
        GalaxyDto galaxyDto = TestUtils.buildGalaxyDtoForAdd();
        MockHttpServletResponse response = performRequestWithBody(post("/galaxy/add"), galaxyDto);
        GalaxyDto addedGalaxy = readResponse(response, GalaxyDto.class);

        // ----------------------------------------test add----------------------------------------

        // given

        // add entity
        StarDto dto1 = TestUtils.buildStarDtoForAdd();
        dto1.setName("name1");
        dto1.getGalaxy().setId(addedGalaxy.getId());

        response = performRequestWithBody(post("/star/add"), dto1);
        StarDto addedDto1 = readResponse(response, StarDto.class);

        // add another entity
        StarDto dto2 = TestUtils.buildStarDtoForAdd();
        dto2.setName("name2");
        dto2.getGalaxy().setId(addedGalaxy.getId());

        response = performRequestWithBody(post("/star/add"), dto2);
        StarDto addedDto2 = readResponse(response, StarDto.class);

        // when
        response = performRequest(get("/star/get-list"));

        // then
        JsonPage<StarDto> resultList = readGenericPageResponse(response);
        assertThat(resultList.getContent())
            .isEqualTo(List.of(addedDto1, addedDto2))
            .allMatch(item -> item.getGalaxy().getId().equals(addedGalaxy.getId()));

        // ----------------------------------------test get----------------------------------------

        // given
        UUID id = addedDto1.getId();

        // when
        response = performRequest(get("/star/get/{id}", id));

        // then
        StarDto result = readResponse(response, StarDto.class);
        assertThat(result).isEqualTo(addedDto1);

        // ----------------------------------------test update----------------------------------------

        // given
        addedDto1.setName(addedDto1.getName() + "Update");
        response = performRequestWithBody(put("/star/update"), addedDto1);
        StarDto updatedDto1 = readResponse(response, StarDto.class);

        id = addedDto1.getId();

        // when
        response = performRequest(get("/star/get/{id}", id));

        // then
        result = readResponse(response, StarDto.class);
        assertThat(result).isEqualTo(updatedDto1);

        // ----------------------------------------test getList----------------------------------------

        // given
        var nameFilter = "1uP";

        // when
        response = performRequest(get("/star/get-list")
            .param("name", nameFilter)
        );

        // then
        resultList = readGenericPageResponse(response);
        assertThat(resultList.getContent()).isEqualTo(List.of(updatedDto1));

        // ----------------------------------------test delete----------------------------------------

        // given
        performRequest(delete("/star/delete/{id}", addedDto1.getId()));
        performRequest(delete("/star/delete/{id}", addedDto2.getId()));

        // when
        response = performRequest(get("/star/get-list"));

        // then
        resultList = readGenericPageResponse(response);
        assertThat(resultList.getContent()).isEmpty();

        // ----------------------------------------test application events----------------------------------------

        verifyEventsByType(Map.ofEntries(
            Map.entry(EventType.GALAXY_ADD.toString(), 1L),
            Map.entry(EventType.STAR_ADD.toString(), 2L),
            Map.entry(EventType.STAR_UPDATE.toString(), 1L),
            Map.entry(EventType.STAR_DELETE.toString(), 2L)
        ));

        // ----------------------------------------cleanup----------------------------------------

        performRequest(delete("/galaxy/delete/{id}", addedGalaxy.getId()));
    }
}
