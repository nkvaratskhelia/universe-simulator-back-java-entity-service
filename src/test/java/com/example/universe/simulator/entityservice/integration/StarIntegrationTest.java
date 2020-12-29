package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
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

class StarIntegrationTest extends AbstractIntegrationTest {

    @Test
    void test() throws Exception {
        //-----------------------------------should add galaxy-----------------------------------

        //given
        GalaxyDto galaxyDto = TestUtils.buildGalaxyDtoForAdd();
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/galaxy/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(galaxyDto))
        ).andReturn().getResponse();
        //then
        GalaxyDto addedGalaxy = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);

        //-----------------------------------should throw sort parameter error-----------------------------------

        //when
        response = mockMvc.perform(get("/star/get-list")
                .param("sort", "invalid")
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.INVALID_SORT_PARAMETER);

        //-----------------------------------should return empty list-----------------------------------

        //when
        response = mockMvc.perform(get("/star/get-list")).andReturn().getResponse();
        //then
        JsonPage<StarDto> resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).isEmpty();

        //-----------------------------------should add entity-----------------------------------

        //given
        StarDto dto = TestUtils.buildStarDtoForAdd();
        dto.setName("name1");
        dto.getGalaxy().setId(addedGalaxy.getId());
        //when
        response = mockMvc.perform(post("/star/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        StarDto addedDto1 = objectMapper.readValue(response.getContentAsString(), StarDto.class);

        //-----------------------------------should add entity-----------------------------------

        //given
        dto = TestUtils.buildStarDtoForAdd();
        dto.setName("name2");
        dto.getGalaxy().setId(addedGalaxy.getId());
        //when
        response = mockMvc.perform(post("/star/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        StarDto addedDto2 = objectMapper.readValue(response.getContentAsString(), StarDto.class);

        //-----------------------------------should return list with 2 elements-----------------------------------

        //when
        response = mockMvc.perform(get("/star/get-list")).andReturn().getResponse();
        //then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).hasSize(2);

        //-----------------------------------should return entity-----------------------------------

        //when
        response = mockMvc.perform(get("/star/get/{id}", addedDto1.getId())).andReturn().getResponse();
        //then
        StarDto resultDto = objectMapper.readValue(response.getContentAsString(), StarDto.class);
        assertThat(resultDto).isEqualTo(addedDto1);

        //-----------------------------------should update entity-----------------------------------

        //given
        dto = TestUtils.buildStarDtoForUpdate();
        dto.setId(addedDto1.getId());
        dto.setName("name1Update");
        dto.getGalaxy().setId(addedGalaxy.getId());

        //when
        response = mockMvc.perform(put("/star/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        StarDto updatedDto = objectMapper.readValue(response.getContentAsString(), StarDto.class);
        assertThat(updatedDto.getName()).isEqualTo("name1Update");
        assertThat(updatedDto.getVersion()).isEqualTo(1);

        //-----------------------------------should throw entity modified error-----------------------------------

        //given
        dto = TestUtils.buildStarDtoForUpdate();
        dto.setId(addedDto1.getId());
        dto.getGalaxy().setId(addedGalaxy.getId());
        //when
        response = mockMvc.perform(put("/star/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_MODIFIED);

        //-----------------------------------should delete entity-----------------------------------

        //when
        response = mockMvc.perform(delete("/star/delete/{id}", addedDto1.getId())).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());

        //-----------------------------------should delete entity-----------------------------------

        //when
        response = mockMvc.perform(delete("/star/delete/{id}", addedDto2.getId())).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());

        //-----------------------------------should throw not found error-----------------------------------

        //when
        response = mockMvc.perform(delete("/star/delete/{id}", addedDto1.getId())).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.NOT_FOUND_ENTITY);

        //-----------------------------------should return empty list-----------------------------------

        //when
        response = mockMvc.perform(get("/star/get-list")).andReturn().getResponse();
        //then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).isEmpty();

        //-----------------------------------should delete galaxy-----------------------------------

        //when
        response = mockMvc.perform(delete("/galaxy/delete/{id}", addedGalaxy.getId())).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());
    }
}
