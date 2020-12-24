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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

class GalaxyIntegrationTest extends AbstractIntegrationTest {

    @Test
    void test() throws Exception {
        //-----should return empty list-----

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/galaxy/get-list")).andReturn().getResponse();
        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        List<GalaxyDto> resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertTrue(resultList.isEmpty());

        //-----should throw missing parameter name error on add-----

        //given
        GalaxyDto dto = TestUtils.buildSampleGalaxyDtoForAdd();
        dto.setName(null);
        //when
        response = mockMvc.perform(post("/galaxy/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);

        //-----should throw missing parameter name error on add-----

        //given
        dto = TestUtils.buildSampleGalaxyDtoForAdd();
        dto.setName("");
        //when
        response = mockMvc.perform(post("/galaxy/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);

        //-----should throw missing parameter name error on add-----

        //given
        dto = TestUtils.buildSampleGalaxyDtoForAdd();
        dto.setName(" ");
        //when
        response = mockMvc.perform(post("/galaxy/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);

        //-----should fix dirty fields and add entity-----

        //given
        dto = TestUtils.buildSampleGalaxyDtoForAdd();

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
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        GalaxyDto addedDto = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);
        assertNotEquals(dirtyId, addedDto.getId());
        assertEquals("name", addedDto.getName());
        assertEquals(0, addedDto.getVersion());

        //-----should throw entity exists error on add-----

        //given
        dto = TestUtils.buildSampleGalaxyDtoForAdd();
        //when
        response = mockMvc.perform(post("/galaxy/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_EXISTS);

        //-----should return list with 1 element-----

        //when
        response = mockMvc.perform(get("/galaxy/get-list")).andReturn().getResponse();
        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertEquals(1, resultList.size());

        //-----should return entity that was added-----

        //when
        UUID addedDtoId = addedDto.getId();
        response = mockMvc.perform(get("/galaxy/get/{id}", addedDtoId)).andReturn().getResponse();
        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        GalaxyDto resultDto = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);
        assertEquals(addedDto, resultDto);

        //-----should throw missing parameter id error on update-----

        //given
        dto = TestUtils.buildSampleGalaxyDtoForUpdate();
        dto.setId(null);
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_ID);

        //-----should throw missing parameter name error on update-----

        //given
        dto = TestUtils.buildSampleGalaxyDtoForUpdate();
        dto.setId(addedDtoId);
        dto.setName(null);
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);

        //-----should throw missing parameter name error on update-----

        //given
        dto = TestUtils.buildSampleGalaxyDtoForUpdate();
        dto.setId(addedDtoId);
        dto.setName("");
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);

        //-----should throw missing parameter name error on update-----

        //given
        dto = TestUtils.buildSampleGalaxyDtoForUpdate();
        dto.setId(addedDtoId);
        dto.setName(" ");
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);

        //-----should throw missing parameter version error on update-----

        //given
        dto = TestUtils.buildSampleGalaxyDtoForUpdate();
        dto.setId(addedDtoId);
        dto.setVersion(null);
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_VERSION);

        //-----should fix dirty fields and update entity-----

        //given
        dto = TestUtils.buildSampleGalaxyDtoForUpdate();
        dto.setId(addedDtoId);

        //dirty input
        dto.setName(" newName ");

        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        GalaxyDto updatedDto = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);
        assertEquals(addedDtoId, updatedDto.getId());
        assertEquals("newName", updatedDto.getName());
        assertEquals(1, updatedDto.getVersion());

        //-----should throw entity modified error on update-----

        //given
        dto = TestUtils.buildSampleGalaxyDtoForUpdate();
        dto.setId(addedDtoId);
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_MODIFIED);

        //-----should delete entity-----

        //when
        response = mockMvc.perform(delete("/galaxy/delete/{id}", addedDtoId)).andReturn().getResponse();
        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        //-----should return empty list-----

        //when
        response = mockMvc.perform(get("/galaxy/get-list")).andReturn().getResponse();
        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertTrue(resultList.isEmpty());

        //-----should throw entity not found error on get-----

        //when
        response = mockMvc.perform(get("/galaxy/get/{id}", addedDtoId)).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_NOT_FOUND);

        //-----should throw entity not found error on update-----

        //given
        dto = TestUtils.buildSampleGalaxyDtoForUpdate();
        dto.setId(addedDtoId);
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_NOT_FOUND);

        //-----should throw entity not found error on delete-----

        //when
        mockMvc.perform(delete("/galaxy/delete/{id}", addedDtoId)).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_NOT_FOUND);
    }
}
