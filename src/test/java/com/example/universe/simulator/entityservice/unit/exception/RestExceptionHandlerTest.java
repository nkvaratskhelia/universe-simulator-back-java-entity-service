package com.example.universe.simulator.entityservice.unit.exception;

import com.example.universe.simulator.entityservice.controllers.GalaxyController;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.services.GalaxyService;
import com.example.universe.simulator.entityservice.unit.AbstractWebMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

/**
 * Generic RestExceptionHandler cases are tested using GalaxyController.
 */
@WebMvcTest(GalaxyController.class)
public class RestExceptionHandlerTest extends AbstractWebMvcTest {

    @MockBean
    private GalaxyService service;

    @Test
    void testWrongHttpMethod() throws Exception {
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/galaxy/get-list")).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.WRONG_HTTP_METHOD);
        then(service).should(never()).getList();
    }

    @Test
    void testHttpMediaTypeNotSupported() throws Exception {
        //given
        Galaxy entity = Galaxy.builder().name("name").build();
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/galaxy/add")
                .content(objectMapper.writeValueAsString(entity))
        ).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.BAD_REQUEST);
        then(service).should(never()).add(entity);
    }

    @Test
    void testHttpMessageNotReadable() throws Exception {
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/galaxy/add")).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.BAD_REQUEST);
    }

    @Test
    void testMethodArgumentTypeMismatch() throws Exception {
        //given
        String id = "id";
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/galaxy/get/{id}", id)).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.BAD_REQUEST);
    }

    @Test
    void testObjectOptimisticLockingFailure() throws Exception {
        //given
        UUID id = UUID.randomUUID();
        Galaxy entity = Galaxy.builder().id(id).name("name").build();
        given(service.update(any())).willThrow(ObjectOptimisticLockingFailureException.class);
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entity))
        ).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_MODIFIED);
        then(service).should().update(entity);
    }

    @Test
    void testUnknownException() throws Exception {
        //given
        given(service.getList()).willThrow(RuntimeException.class);
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/galaxy/get-list")).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.SERVER_ERROR);
        then(service).should().getList();
    }
}
