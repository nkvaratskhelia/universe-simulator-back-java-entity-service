package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.utils.TestUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

class GalaxyIntegrationTest extends AbstractIntegrationTest {

    @Test
    void test() throws Exception {
        //-----------------------------------should return empty list-----------------------------------

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/galaxy/get-list")).andReturn().getResponse();
        //then
        List<GalaxyDto> resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList).isEmpty();

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
        response = mockMvc.perform(get("/galaxy/get-list")).andReturn().getResponse();
        //then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList).hasSize(2);

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
        assertThat(updatedDto.getName()).isEqualTo("name1Update");
        assertThat(updatedDto.getVersion()).isEqualTo(1);

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
        response = mockMvc.perform(get("/galaxy/get-list")).andReturn().getResponse();
        //then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList).isEmpty();
    }
}
