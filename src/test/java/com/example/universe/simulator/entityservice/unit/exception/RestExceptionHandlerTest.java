package com.example.universe.simulator.entityservice.unit.exception;

import com.example.universe.simulator.entityservice.controllers.GalaxyController;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.services.GalaxyService;
import com.example.universe.simulator.entityservice.unit.AbstractWebMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
        then(service).should(never()).getList();
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.WRONG_HTTP_METHOD);
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
    void testUnknownException() throws Exception {
        //given
        given(service.getList()).willThrow(RuntimeException.class);
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/galaxy/get-list")).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.SERVER_ERROR);
    }
}
