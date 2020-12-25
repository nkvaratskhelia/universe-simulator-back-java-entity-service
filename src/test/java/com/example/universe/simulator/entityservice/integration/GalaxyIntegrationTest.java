package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.utils.TestUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
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

        //-----------------------------------should throw missing parameter name error on add-----------------------------------

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

        //-----------------------------------should throw missing parameter name error on add-----------------------------------

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

        //-----------------------------------should throw missing parameter name error on add-----------------------------------

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
        dto.setName(" name ");
        dto.setVersion(1L);

        //when
        response = mockMvc.perform(post("/galaxy/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());

        GalaxyDto addedDto = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);
        assertThat(addedDto.getId()).isNotEqualTo(dirtyId);
        assertThat(addedDto.getName()).isEqualTo("name");
        assertThat(addedDto.getVersion()).isEqualTo(0);

        //-----------------------------------should throw entity exists error on add-----------------------------------

        //given
        dto = TestUtils.buildGalaxyDtoForAdd();
        //when
        response = mockMvc.perform(post("/galaxy/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_EXISTS);

        //-----------------------------------should return list with 1 element-----------------------------------

        //when
        response = mockMvc.perform(get("/galaxy/get-list")).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList).hasSize(1);

        //-----------------------------------should return entity that was added-----------------------------------

        //when
        UUID id = addedDto.getId();
        response = mockMvc.perform(get("/galaxy/get/{id}", id)).andReturn().getResponse();
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        GalaxyDto resultDto = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);
        assertThat(resultDto).isEqualTo(addedDto);

        //-----------------------------------should throw missing parameter id error on update-----------------------------------

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

        //-----------------------------------should throw missing parameter name error on update-----------------------------------

        //given
        dto = TestUtils.buildGalaxyDtoForUpdate();
        dto.setId(id);
        dto.setName(null);
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);

        //-----------------------------------should throw missing parameter name error on update-----------------------------------

        //given
        dto = TestUtils.buildGalaxyDtoForUpdate();
        dto.setId(id);
        dto.setName("");
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);

        //-----------------------------------should throw missing parameter name error on update-----------------------------------

        //given
        dto = TestUtils.buildGalaxyDtoForUpdate();
        dto.setId(id);
        dto.setName(" ");
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);

        //-----------------------------------should throw missing parameter version error on update-----------------------------------

        //given
        dto = TestUtils.buildGalaxyDtoForUpdate();
        dto.setId(id);
        dto.setVersion(null);
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_VERSION);

        //-----------------------------------should fix dirty fields and update entity-----------------------------------

        //given
        dto = TestUtils.buildGalaxyDtoForUpdate();
        dto.setId(id);

        //dirty input
        dto.setName(" newName ");

        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());

        GalaxyDto updatedDto = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);
        assertThat(updatedDto.getId()).isEqualTo(id);
        assertThat(updatedDto.getName()).isEqualTo("newName");
        assertThat(updatedDto.getVersion()).isEqualTo(1);

        //-----------------------------------should throw entity modified error on update-----------------------------------

        //given
        dto = TestUtils.buildGalaxyDtoForUpdate();
        dto.setId(id);
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_MODIFIED);

        //-----------------------------------should delete entity-----------------------------------

        //when
        response = mockMvc.perform(delete("/galaxy/delete/{id}", id)).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());

        //-----------------------------------should return empty list-----------------------------------

        //when
        response = mockMvc.perform(get("/galaxy/get-list")).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList).isEmpty();

        //-----------------------------------should throw entity not found error on get-----------------------------------

        //when
        response = mockMvc.perform(get("/galaxy/get/{id}", id)).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_NOT_FOUND);

        //-----------------------------------should throw entity not found error on update-----------------------------------

        //given
        dto = TestUtils.buildGalaxyDtoForUpdate();
        dto.setId(id);
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_NOT_FOUND);

        //-----------------------------------should throw entity not found error on delete-----------------------------------

        //when
        mockMvc.perform(delete("/galaxy/delete/{id}", id)).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_NOT_FOUND);
    }
}
