package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.common.utils.JsonPage;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.filters.GalaxyFilter;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

class GalaxyIntegrationTest extends AbstractIntegrationTest {

    @Test
    void test() throws Exception {
        // -----------------------------------should return empty list-----------------------------------

        // when
        MockHttpServletResponse response = performRequest(post("/galaxy/get-list"));
        // then
        JsonPage<GalaxyDto> resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).isEmpty();

        // -----------------------------------should add entity-----------------------------------

        // given
        GalaxyDto dto = TestUtils.buildGalaxyDtoForAdd();
        dto.setName("name1");
        // when
        response = performRequest(post("/galaxy/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
        );
        // then
        GalaxyDto addedDto1 = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);

        // -----------------------------------should add entity-----------------------------------

        // given
        dto = TestUtils.buildGalaxyDtoForAdd();
        dto.setName("name2");
        // when
        response = performRequest(post("/galaxy/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
        );
        // then
        GalaxyDto addedDto2 = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);

        // -----------------------------------should return list with 2 elements-----------------------------------

        // when
        response = performRequest(post("/galaxy/get-list"));
        // then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).hasSize(2);

        // -----------------------------------should return entity-----------------------------------

        // when
        response = performRequest(get("/galaxy/get/{id}", addedDto1.getId()));
        // then
        GalaxyDto resultDto = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);
        assertThat(resultDto).isEqualTo(addedDto1);

        // -----------------------------------should update entity-----------------------------------

        // given
        dto = TestUtils.buildGalaxyDtoForUpdate();
        dto.setId(addedDto1.getId());
        dto.setName("name1Update");

        // when
        performRequest(put("/galaxy/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
        );
        response = performRequest(get("/galaxy/get/{id}", addedDto1.getId()));

        // then
        resultDto = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);
        assertThat(resultDto.getName()).isEqualTo(dto.getName());
        assertThat(resultDto.getVersion()).isEqualTo(dto.getVersion() + 1);

        // -----------------------------------should return list with 1 element-----------------------------------

        // given
        GalaxyFilter filter = TestUtils.buildGalaxyFilter();
        filter.setName("1uP");
        // when
        response = performRequest(post("/galaxy/get-list")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(filter))
        );
        // then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).hasSize(1);

        // -----------------------------------should delete entity-----------------------------------

        // when
        response = performRequest(delete("/galaxy/delete/{id}", addedDto1.getId()));
        // then
        verifyOkStatus(response.getStatus());

        // -----------------------------------should delete entity-----------------------------------

        // when
        response = performRequest(delete("/galaxy/delete/{id}", addedDto2.getId()));
        // then
        verifyOkStatus(response.getStatus());

        // -----------------------------------should return empty list-----------------------------------

        // when
        response = performRequest(post("/galaxy/get-list"));
        // then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).isEmpty();
    }
}
