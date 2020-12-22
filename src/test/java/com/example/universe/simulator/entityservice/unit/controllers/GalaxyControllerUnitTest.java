package com.example.universe.simulator.entityservice.unit.controllers;

import com.example.universe.simulator.entityservice.controllers.GalaxyController;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.services.GalaxyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ActiveProfiles("test")
@WebMvcTest(GalaxyController.class)
public class GalaxyControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
        then(service).should().getList();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(objectMapper.writeValueAsString(list), response.getContentAsString());
    }

    @Test
    void testGet_idNotFound() {
        //given
        UUID id = UUID.randomUUID();
        given(service.get(any())).willThrow(NoSuchElementException.class);
        //then
        NestedServletException exception = assertThrows(NestedServletException.class, () -> mockMvc.perform(get("/galaxy/get/{id}", id)));
        then(service).should().get(id);
        assertEquals(NoSuchElementException.class, exception.getCause().getClass());
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
        then(service).should().get(id);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(objectMapper.writeValueAsString(entity), response.getContentAsString());
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
        then(service).should().add(entity);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(objectMapper.writeValueAsString(entity), response.getContentAsString());
    }

    @Test
    void testUpdate_idNotFound() {
        //given
        UUID id = UUID.randomUUID();
        Galaxy entity = Galaxy.builder().id(id).name("name").build();
        given(service.update(any())).willThrow(NoSuchElementException.class);
        //then
        NestedServletException exception = assertThrows(NestedServletException.class, () -> mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entity))
        ));
        then(service).should().update(entity);
        assertEquals(NoSuchElementException.class, exception.getCause().getClass());
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
        then(service).should().update(entity);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(objectMapper.writeValueAsString(entity), response.getContentAsString());
    }

    @Test
    void testDelete_idNotFound() {
        //given
        UUID id = UUID.randomUUID();
        willThrow(NoSuchElementException.class).given(service).delete(any());
        //then
        NestedServletException exception = assertThrows(NestedServletException.class, () -> mockMvc.perform(delete("/galaxy/delete/{id}", id)));
        then(service).should().delete(id);
        assertEquals(NoSuchElementException.class, exception.getCause().getClass());
    }

    @Test
    void testDelete_successfulDelete() throws Exception {
        //given
        UUID id = UUID.randomUUID();
        //when
        MockHttpServletResponse response = mockMvc.perform(delete("/galaxy/delete/{id}", id)).andReturn().getResponse();
        //then
        then(service).should().delete(id);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
}
