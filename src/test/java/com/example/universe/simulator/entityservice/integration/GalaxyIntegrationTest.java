package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

public class GalaxyIntegrationTest extends AbstractIntegrationTest {

    @Test
    void test() throws Exception {
        //-----should return empty list-----

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/galaxy/get-list")).andReturn().getResponse();
        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        List<Galaxy> resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertTrue(resultList.isEmpty());

        //-----should add entity-----

        //given
        Galaxy entity = Galaxy.builder().name("name").build();
        //when
        response = mockMvc.perform(post("/galaxy/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entity))
        ).andReturn().getResponse();
        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        Galaxy addedEntity = objectMapper.readValue(response.getContentAsString(), Galaxy.class);
        assertNotNull(addedEntity.getId());
        assertEquals(entity.getName(), addedEntity.getName());
        assertEquals(0, addedEntity.getVersion());

        //-----should return list with 1 element-----

        //when
        response = mockMvc.perform(get("/galaxy/get-list")).andReturn().getResponse();
        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertEquals(1, resultList.size());

        //-----should return entity that was added-----

        //when
        UUID id = addedEntity.getId();
        response = mockMvc.perform(get("/galaxy/get/{id}", id)).andReturn().getResponse();
        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        Galaxy resultEntity = objectMapper.readValue(response.getContentAsString(), Galaxy.class);
        assertEquals(addedEntity, resultEntity);

        //-----should update entity-----

        //given
        entity = Galaxy.builder().id(id).name("newName").build();
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entity))
        ).andReturn().getResponse();
        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        Galaxy updatedEntity = objectMapper.readValue(response.getContentAsString(), Galaxy.class);
        assertEquals(id, updatedEntity.getId());
        assertEquals(entity.getName(), updatedEntity.getName());
        assertEquals(1, updatedEntity.getVersion());

        //-----should throw entity modified exception on update-----

        //given
        entity = Galaxy.builder().id(id).name("newName").build();
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entity))
        ).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_MODIFIED);

        //-----should delete entity-----

        //when
        response = mockMvc.perform(delete("/galaxy/delete/{id}", id)).andReturn().getResponse();
        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        //-----should return empty list-----

        //when
        response = mockMvc.perform(get("/galaxy/get-list")).andReturn().getResponse();
        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertTrue(resultList.isEmpty());
        
        //-----should throw entity not found exception on get-----
        
        //when
        response = mockMvc.perform(get("/galaxy/get/{id}", id)).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_NOT_FOUND);

        //-----should throw entity not found exception on update-----

        //given
        entity = Galaxy.builder().id(id).build();
        //when
        response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entity))
        ).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_NOT_FOUND);

        //-----should throw entity not found exception on delete-----

        //when
        mockMvc.perform(delete("/galaxy/delete/{id}", id)).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_NOT_FOUND);
    }
}
