package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.utils.TestUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;
import java.util.UUID;

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
        verifyOkStatus(response.getStatus());
        List<GalaxyDto> resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList).isEmpty();

        //-----------------------------------should throw missing name error when null name-----------------------------------

        //given
        GalaxyDto dto = TestUtils.buildGalaxyDtoForAdd();
        dto.setName(null);
        //when
        response = mockMvc.perform(post("/galaxy/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);

        //-----------------------------------should throw missing name error when empty name-----------------------------------

        //given
        dto = TestUtils.buildGalaxyDtoForAdd();
        dto.setName("");
        //when
        response = mockMvc.perform(post("/galaxy/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);

        //-----------------------------------should throw missing name error when blank name-----------------------------------

        //given
        dto = TestUtils.buildGalaxyDtoForAdd();
        dto.setName(" ");
        //when
        response = mockMvc.perform(post("/galaxy/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);

        //-----------------------------------should fix dirty fields and add entity-----------------------------------

        //given
        dto = TestUtils.buildGalaxyDtoForAdd();

        //dirty input
        UUID dirtyId = UUID.randomUUID();
        dto.setId(dirtyId);
        dto.setName(" name1 ");
        dto.setVersion(1L);

        //when
        response = mockMvc.perform(post("/galaxy/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());

        GalaxyDto addedDto1 = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);
        assertThat(addedDto1.getId()).isNotEqualTo(dirtyId);
        assertThat(addedDto1.getName()).isEqualTo("name1");
        assertThat(addedDto1.getVersion()).isEqualTo(0);

        //-----------------------------------should return list with 1 element-----------------------------------

        //when
        response = mockMvc.perform(get("/galaxy/get-list")).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList).hasSize(1);

        //-----------------------------------should throw name exists error-----------------------------------

        //given
        dto = TestUtils.buildGalaxyDtoForAdd();
        dto.setName(addedDto1.getName());
        //when
        response = mockMvc.perform(post("/galaxy/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.EXISTS_NAME);

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
        verifyOkStatus(response.getStatus());
        GalaxyDto addedDto2 = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);
        assertThat(addedDto2.getName()).isEqualTo("name2");

        //-----------------------------------should return list with 2 elements-----------------------------------

        //when
        response = mockMvc.perform(get("/galaxy/get-list")).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList).hasSize(2);

        //-----------------------------------should return entity-----------------------------------

        //when
        response = mockMvc.perform(get("/galaxy/get/{id}", addedDto1.getId())).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());
        GalaxyDto resultDto = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);
        assertThat(resultDto).isEqualTo(addedDto1);

        //-----------------------------------should throw missing id error-----------------------------------

        //given
        dto = TestUtils.buildGalaxyDtoForUpdate();
        dto.setId(null);
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_ID);

        //-----------------------------------should throw missing name error when null name-----------------------------------

        //given
        dto = TestUtils.buildGalaxyDtoForUpdate();
        dto.setId(addedDto1.getId());
        dto.setName(null);
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);

        //-----------------------------------should throw missing name error when empty name-----------------------------------

        //given
        dto = TestUtils.buildGalaxyDtoForUpdate();
        dto.setId(addedDto1.getId());
        dto.setName("");
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);

        //-----------------------------------should throw missing name error when blank name-----------------------------------

        //given
        dto = TestUtils.buildGalaxyDtoForUpdate();
        dto.setId(addedDto1.getId());
        dto.setName(" ");
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);

        //-----------------------------------should throw missing version error-----------------------------------

        //given
        dto = TestUtils.buildGalaxyDtoForUpdate();
        dto.setId(addedDto1.getId());
        dto.setVersion(null);
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_VERSION);

        //-----------------------------------should throw name exists error-----------------------------------

        //given
        dto = TestUtils.buildGalaxyDtoForUpdate();
        dto.setId(addedDto1.getId());
        dto.setName(addedDto2.getName());
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.EXISTS_NAME);

        //-----------------------------------should fix dirty fields and update entity-----------------------------------

        //given
        dto = TestUtils.buildGalaxyDtoForUpdate();
        dto.setId(addedDto1.getId());

        //dirty input
        dto.setName(" name1Update ");

        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());

        GalaxyDto updatedDto = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);
        assertThat(updatedDto.getId()).isEqualTo(addedDto1.getId());
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

        //-----------------------------------should return list with 1 element-----------------------------------

        //when
        response = mockMvc.perform(get("/galaxy/get-list")).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList).hasSize(1);

        //-----------------------------------should throw not found error-----------------------------------

        //when
        response = mockMvc.perform(get("/galaxy/get/{id}", addedDto1.getId())).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_NOT_FOUND);

        //-----------------------------------should throw not found error-----------------------------------

        //given
        dto = TestUtils.buildGalaxyDtoForUpdate();
        dto.setId(addedDto1.getId());
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_NOT_FOUND);

        //-----------------------------------should throw not found error-----------------------------------

        //when
        mockMvc.perform(delete("/galaxy/delete/{id}", addedDto1.getId())).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_NOT_FOUND);
    }
}
