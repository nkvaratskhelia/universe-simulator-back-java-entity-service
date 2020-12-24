package com.example.universe.simulator.entityservice.unit.controllers;

import com.example.universe.simulator.entityservice.controllers.GalaxyController;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.services.GalaxyService;
import com.example.universe.simulator.entityservice.unit.AbstractWebMvcTest;
import com.example.universe.simulator.entityservice.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.modelmapper.TypeToken;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(GalaxyController.class)
class GalaxyControllerTest extends AbstractWebMvcTest {

    @MockBean
    private GalaxyService service;

    @Test
    void testGetList() throws Exception {
        //given
        List<Galaxy> entityList = List.of(
                Galaxy.builder().name("name").build()
        );
        List<GalaxyDto> dtoList = modelMapper.map(entityList, new TypeToken<List<GalaxyDto>>() {}.getType());

        given(service.getList()).willReturn(entityList);
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/galaxy/get-list")).andReturn().getResponse();
        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(objectMapper.writeValueAsString(dtoList), response.getContentAsString());

        then(service).should().getList();
    }

    @Test
    void testGet_idNotFound() throws Exception {
        //given
        UUID id = UUID.randomUUID();
        given(service.get(any())).willThrow(new AppException(ErrorCodeType.ENTITY_NOT_FOUND));
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/galaxy/get/{id}", id)).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_NOT_FOUND);
        then(service).should().get(id);
    }

    @Test
    void testGet_successfulGet() throws Exception {
        //given
        UUID id = UUID.randomUUID();
        Galaxy entity = Galaxy.builder().name("name").build();
        GalaxyDto dto = modelMapper.map(entity, GalaxyDto.class);

        given(service.get(any())).willReturn(entity);
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/galaxy/get/{id}", id)).andReturn().getResponse();
        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(objectMapper.writeValueAsString(dto), response.getContentAsString());

        then(service).should().get(id);
    }

    @Test
    void testAdd_validationFail() throws Exception {
        //-----should fail validation on null name-----

        //given
        GalaxyDto dto = TestUtils.buildSampleGalaxyDtoForAdd();
        dto.setName(null);
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/galaxy/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);
        then(service).should(never()).add(any());

        //-----should fail validation on empty name-----

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
        then(service).should(never()).add(any());

        //-----should fail validation on blank name-----

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
        then(service).should(never()).add(any());
    }

    @Test
    void testAdd_dirtyFieldFixAndSuccessfulAdd() throws Exception {
        //given
        GalaxyDto inputDto = TestUtils.buildSampleGalaxyDtoForAdd();
        Galaxy entity = modelMapper.map(inputDto, Galaxy.class);
        GalaxyDto resultDto = modelMapper.map(entity, GalaxyDto.class);
        
        //dirty input
        inputDto.setId(UUID.randomUUID());
        inputDto.setName(" name ");
        inputDto.setVersion(1L);

        given(service.add(any())).willReturn(entity);
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/galaxy/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto))
        ).andReturn().getResponse();
        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(objectMapper.writeValueAsString(resultDto), response.getContentAsString());

        then(service).should().add(entity);
    }

    @Test
    void testUpdate_validationFail() throws Exception {
        //-----should fail validation on null id-----

        //given
        GalaxyDto dto = new GalaxyDto();
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_ID);
        then(service).should(never()).update(any());

        //-----should fail validation on null name-----

        //given
        dto = TestUtils.buildSampleGalaxyDtoForUpdate();
        dto.setName(null);
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);
        then(service).should(never()).update(any());

        //-----should fail validation on empty name-----

        //given
        dto = TestUtils.buildSampleGalaxyDtoForUpdate();
        dto.setName("");
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);
        then(service).should(never()).update(any());

        //-----should fail validation on blank name-----

        //given
        dto = TestUtils.buildSampleGalaxyDtoForUpdate();
        dto.setName(" ");
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);
        then(service).should(never()).update(any());

        //-----should fail validation on null version-----

        //given
        dto = TestUtils.buildSampleGalaxyDtoForUpdate();
        dto.setVersion(null);
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_VERSION);
        then(service).should(never()).update(any());
    }

    @Test
    void testUpdate_idNotFound() throws Exception {
        //given
        GalaxyDto dto = TestUtils.buildSampleGalaxyDtoForUpdate();
        Galaxy entity = modelMapper.map(dto, Galaxy.class);

        given(service.update(any())).willThrow(new AppException(ErrorCodeType.ENTITY_NOT_FOUND));
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_NOT_FOUND);
        then(service).should().update(entity);
    }

    @Test
    void testUpdate_dirtyFieldFixAndSuccessfulUpdate() throws Exception {
        //given
        GalaxyDto inputDto = TestUtils.buildSampleGalaxyDtoForUpdate();
        Galaxy entity = modelMapper.map(inputDto, Galaxy.class);
        GalaxyDto resultDto = modelMapper.map(entity, GalaxyDto.class);

        //dirty input
        inputDto.setName(" name ");

        given(service.update(any())).willReturn(entity);
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto))
        ).andReturn().getResponse();
        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(objectMapper.writeValueAsString(resultDto), response.getContentAsString());

        then(service).should().update(entity);
    }

    @Test
    void testDelete_idNotFound() throws Exception {
        //given
        UUID id = UUID.randomUUID();
        willThrow(new AppException(ErrorCodeType.ENTITY_NOT_FOUND)).given(service).delete(any());
        //when
        MockHttpServletResponse response = mockMvc.perform(delete("/galaxy/delete/{id}", id)).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_NOT_FOUND);
        then(service).should().delete(id);
    }

    @Test
    void testDelete_successfulDelete() throws Exception {
        //given
        UUID id = UUID.randomUUID();
        //when
        MockHttpServletResponse response = mockMvc.perform(delete("/galaxy/delete/{id}", id)).andReturn().getResponse();
        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        then(service).should().delete(id);
    }
}
