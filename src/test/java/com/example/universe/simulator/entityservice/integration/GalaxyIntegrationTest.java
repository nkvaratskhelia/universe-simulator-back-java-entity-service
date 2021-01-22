package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.filters.GalaxyFilter;
import com.example.universe.simulator.entityservice.common.utils.JsonPage;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
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
        //-----------------------------------should throw sort parameter error-----------------------------------

        //when
        MockHttpServletResponse response = performRequest(post("/galaxy/get-list")
                .param("sort", "invalid")
        );
        //then
        verifyErrorResponse(response, ErrorCodeType.INVALID_SORT_PARAMETER);

        //-----------------------------------should return empty list-----------------------------------

        //when
        response = performRequest(post("/galaxy/get-list"));
        //then
        JsonPage<GalaxyDto> resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).isEmpty();

        //-----------------------------------should add entity-----------------------------------

        //given
        GalaxyDto dto = TestUtils.buildGalaxyDtoForAdd();
        dto.setName("name1");
        //when
        response = performRequest(post("/galaxy/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        );
        //then
        GalaxyDto addedDto1 = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);

        //-----------------------------------should add entity-----------------------------------

        //given
        dto = TestUtils.buildGalaxyDtoForAdd();
        dto.setName("name2");
        //when
        response = performRequest(post("/galaxy/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        );
        //then
        GalaxyDto addedDto2 = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);

        //-----------------------------------should return list with 2 elements-----------------------------------

        //when
        response = performRequest(post("/galaxy/get-list"));
        //then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).hasSize(2);

        //-----------------------------------should return entity-----------------------------------

        //when
        response = performRequest(get("/galaxy/get/{id}", addedDto1.getId()));
        //then
        GalaxyDto resultDto = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);
        assertThat(resultDto).isEqualTo(addedDto1);

        //-----------------------------------should update entity-----------------------------------

        //given
        dto = TestUtils.buildGalaxyDtoForUpdate();
        dto.setId(addedDto1.getId());
        dto.setName("name1Update");

        //when
        response = performRequest(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        );
        //then
        GalaxyDto updatedDto = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);
        assertThat(updatedDto.getName()).isEqualTo(dto.getName());
        assertThat(updatedDto.getVersion()).isEqualTo(dto.getVersion() + 1);

        //-----------------------------------should throw entity modified error-----------------------------------

        //given
        dto = TestUtils.buildGalaxyDtoForUpdate();
        dto.setId(addedDto1.getId());
        //when
        response = performRequest(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        );
        //then
        verifyErrorResponse(response, ErrorCodeType.ENTITY_MODIFIED);

        //-----------------------------------should return list with 1 element-----------------------------------

        //given
        GalaxyFilter filter = GalaxyFilter.builder().name("1uP").build();
        //when
        response = performRequest(post("/galaxy/get-list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filter))
        );
        //then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).hasSize(1);

        //-----------------------------------should delete entity-----------------------------------

        //when
        response = performRequest(delete("/galaxy/delete/{id}", addedDto1.getId()));
        //then
        verifyOkStatus(response.getStatus());

        //-----------------------------------should delete entity-----------------------------------

        //when
        response = performRequest(delete("/galaxy/delete/{id}", addedDto2.getId()));
        //then
        verifyOkStatus(response.getStatus());

        //-----------------------------------should throw not found error-----------------------------------

        //when
        response = performRequest(delete("/galaxy/delete/{id}", addedDto1.getId()));
        //then
        verifyErrorResponse(response, ErrorCodeType.NOT_FOUND_ENTITY);

        //-----------------------------------should return empty list-----------------------------------

        //when
        response = performRequest(post("/galaxy/get-list"));
        //then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).isEmpty();
    }
}
