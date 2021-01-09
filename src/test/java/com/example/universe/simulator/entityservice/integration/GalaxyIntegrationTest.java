package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.filters.GalaxyFilter;
import com.example.universe.simulator.entityservice.utils.JsonPage;
import com.example.universe.simulator.entityservice.utils.TestUtils;
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
        MockHttpServletResponse response = mockMvc.perform(post("/galaxy/get-list")
                .param("sort", "invalid")
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.INVALID_SORT_PARAMETER);

        //-----------------------------------should return empty list-----------------------------------

        //when
        response = mockMvc.perform(post("/galaxy/get-list")).andReturn().getResponse();
        //then
        JsonPage<GalaxyDto> resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).isEmpty();

        //-----------------------------------should add entity-----------------------------------

        //given
        GalaxyDto dto = TestUtils.buildGalaxyDtoForAdd();
        dto.setName("name1");
        //when
        response = mockMvc.perform(post("/galaxy/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        GalaxyDto addedDto1 = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);

        //-----------------------------------should add entity-----------------------------------

        //given
        dto = TestUtils.buildGalaxyDtoForAdd();
        dto.setName("name2");
        //when
        response = mockMvc.perform(post("/galaxy/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        GalaxyDto addedDto2 = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);

        //-----------------------------------should return list with 2 elements-----------------------------------

        //when
        response = mockMvc.perform(post("/galaxy/get-list")).andReturn().getResponse();
        //then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).hasSize(2);

        //-----------------------------------should return entity-----------------------------------

        //when
        response = mockMvc.perform(get("/galaxy/get/{id}", addedDto1.getId())).andReturn().getResponse();
        //then
        GalaxyDto resultDto = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);
        assertThat(resultDto).isEqualTo(addedDto1);

        //-----------------------------------should update entity-----------------------------------

        //given
        dto = TestUtils.buildGalaxyDtoForUpdate();
        dto.setId(addedDto1.getId());
        dto.setName("name1Update");

        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        GalaxyDto updatedDto = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);
        assertThat(updatedDto.getName()).isEqualTo(dto.getName());
        assertThat(updatedDto.getVersion()).isEqualTo(dto.getVersion() + 1);

        //-----------------------------------should throw entity modified error-----------------------------------

        //given
        dto = TestUtils.buildGalaxyDtoForUpdate();
        dto.setId(addedDto1.getId());
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_MODIFIED);

        //-----------------------------------should return list with 1 element-----------------------------------

        //given
        GalaxyFilter filter = GalaxyFilter.builder().name("1uP").build();
        //when
        response = mockMvc.perform(post("/galaxy/get-list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filter))
        ).andReturn().getResponse();
        //then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).hasSize(1);

        //-----------------------------------should delete entity-----------------------------------

        //when
        response = mockMvc.perform(delete("/galaxy/delete/{id}", addedDto1.getId())).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());

        //-----------------------------------should delete entity-----------------------------------

        //when
        response = mockMvc.perform(delete("/galaxy/delete/{id}", addedDto2.getId())).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());

        //-----------------------------------should throw not found error-----------------------------------

        //when
        response = mockMvc.perform(delete("/galaxy/delete/{id}", addedDto1.getId())).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.NOT_FOUND_ENTITY);

        //-----------------------------------should return empty list-----------------------------------

        //when
        response = mockMvc.perform(post("/galaxy/get-list")).andReturn().getResponse();
        //then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).isEmpty();
    }
}
