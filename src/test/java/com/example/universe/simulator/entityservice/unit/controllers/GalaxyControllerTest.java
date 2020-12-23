package com.example.universe.simulator.entityservice.unit.controllers;

import com.example.universe.simulator.entityservice.controllers.GalaxyController;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.services.GalaxyService;
import com.example.universe.simulator.entityservice.unit.AbstractWebMvcTest;
import org.junit.jupiter.api.Test;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(GalaxyController.class)
public class GalaxyControllerTest extends AbstractWebMvcTest {

    @MockBean
    private GalaxyService service;

    @Test
    void testGetList() throws Exception {
        //given
        List<Galaxy> list = List.of(
                Galaxy.builder().name("name").build()
        );
        given(service.getList()).willReturn(list);
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/galaxy/get-list")).andReturn().getResponse();
        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(objectMapper.writeValueAsString(list), response.getContentAsString());
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
        given(service.get(any())).willReturn(entity);
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/galaxy/get/{id}", id)).andReturn().getResponse();
        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(objectMapper.writeValueAsString(entity), response.getContentAsString());
        then(service).should().get(id);
    }

    @Test
    void testAdd() throws Exception {
        //given
        Galaxy entity = Galaxy.builder().name("name").build();
        given(service.add(any())).willReturn(entity);
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/galaxy/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entity))
        ).andReturn().getResponse();
        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(objectMapper.writeValueAsString(entity), response.getContentAsString());
        then(service).should().add(entity);
    }

    @Test
    void testUpdate_idNotFound() throws Exception {
        //given
        UUID id = UUID.randomUUID();
        Galaxy entity = Galaxy.builder().id(id).name("name").build();
        given(service.update(any())).willThrow(new AppException(ErrorCodeType.ENTITY_NOT_FOUND));
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entity))
        ).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_NOT_FOUND);
        then(service).should().update(entity);
    }

    @Test
    void testUpdate_successfulUpdate() throws Exception {
        //given
        UUID id = UUID.randomUUID();
        Galaxy entity = Galaxy.builder().id(id).name("name").build();
        given(service.update(any())).willReturn(entity);
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entity))
        ).andReturn().getResponse();
        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(objectMapper.writeValueAsString(entity), response.getContentAsString());
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
