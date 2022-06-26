package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.common.utils.JsonPage;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
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

class GalaxyIntegrationTest extends AbstractIntegrationTest {

    @Test
    void test() throws Exception {
        // ----------------------------------------test add----------------------------------------

        // given

        // add entity
        GalaxyDto dto1 = TestUtils.buildGalaxyDtoForAdd();
        dto1.setName("name1");

        MockHttpServletResponse response = performRequestWithBody(post("/galaxy/add"), dto1);
        GalaxyDto addedDto1 = readResponse(response, GalaxyDto.class);

        // add another entity
        GalaxyDto dto2 = TestUtils.buildGalaxyDtoForAdd();
        dto2.setName("name2");

        response = performRequestWithBody(post("/galaxy/add"), dto2);
        GalaxyDto addedDto2 = readResponse(response, GalaxyDto.class);

        // when
        response = performRequest(get("/galaxy/get-list"));

        // then
        JsonPage<GalaxyDto> resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).isEqualTo(List.of(addedDto1, addedDto2));

        // ----------------------------------------test get----------------------------------------

        // given
        UUID id = addedDto1.getId();

        // when
        response = performRequest(get("/galaxy/get/{id}", id));

        // then
        GalaxyDto result = readResponse(response, GalaxyDto.class);
        assertThat(result).isEqualTo(addedDto1);

        // ----------------------------------------test update----------------------------------------

        // given
        addedDto1.setName(addedDto1.getName() + "Update");
        response = performRequestWithBody(put("/galaxy/update"), addedDto1);
        GalaxyDto updatedDto1 = readResponse(response, GalaxyDto.class);

        id = addedDto1.getId();

        // when
        response = performRequest(get("/galaxy/get/{id}", id));

        // then
        result = readResponse(response, GalaxyDto.class);
        assertThat(result).isEqualTo(updatedDto1);

        // ----------------------------------------test getList----------------------------------------

        // given
        var nameFilter = "1uP";

        // when
        response = performRequest(get("/galaxy/get-list")
            .param("name", nameFilter)
        );

        // then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).isEqualTo(List.of(updatedDto1));

        // ----------------------------------------test delete----------------------------------------

        // given
        performRequest(delete("/galaxy/delete/{id}", addedDto1.getId()));
        performRequest(delete("/galaxy/delete/{id}", addedDto2.getId()));

        // when
        response = performRequest(get("/galaxy/get-list"));

        // then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).isEmpty();

        // ----------------------------------------test application events----------------------------------------

        verifyEventsByType(Map.ofEntries(
            Map.entry(EventType.GALAXY_ADD.toString(), 2L),
            Map.entry(EventType.GALAXY_UPDATE.toString(), 1L),
            Map.entry(EventType.GALAXY_DELETE.toString(), 2L)
        ));
    }
}
