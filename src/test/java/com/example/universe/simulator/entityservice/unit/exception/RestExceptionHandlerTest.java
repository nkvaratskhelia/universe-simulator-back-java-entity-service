package com.example.universe.simulator.entityservice.unit.exception;

import com.example.universe.simulator.entityservice.controllers.GalaxyController;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.services.GalaxyService;
import com.example.universe.simulator.entityservice.unit.AbstractWebMvcTest;
import com.example.universe.simulator.entityservice.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

/**
 * Common exception handlers are tested using GalaxyController.
 */
@WebMvcTest(GalaxyController.class)
class RestExceptionHandlerTest extends AbstractWebMvcTest {

    @MockBean
    private GalaxyService service;

    @Test
    void testDataIntegrityViolation() throws Exception {
        //given
        GalaxyDto dto = TestUtils.buildSampleGalaxyDtoForAdd();
        Galaxy entity = modelMapper.map(dto, Galaxy.class);

        given(service.add(any())).willThrow(DataIntegrityViolationException.class);
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/galaxy/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_EXISTS);
        then(service).should().add(entity);
    }

    @Test
    void testHttpMediaTypeNotSupported() throws Exception {
        //-----should fail on missing content type-----

        //given
        GalaxyDto dto = new GalaxyDto();
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/galaxy/add")
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.INVALID_CONTENT_TYPE);
        then(service).should(never()).add(any());

        //-----should fail on invalid content type-----

        //when
        response = mockMvc.perform(post("/galaxy/add")
                .contentType(MediaType.APPLICATION_XML)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.INVALID_CONTENT_TYPE);
        then(service).should(never()).add(any());
    }

    @Test
    void testHttpMessageNotReadable() throws Exception {
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/galaxy/add")).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.INVALID_REQUEST_BODY);
        then(service).should(never()).add(any());
    }

    @Test
    void testHttpRequestMethodNotSupported() throws Exception {
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/galaxy/get-list")).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.INVALID_HTTP_METHOD);
        then(service).should(never()).getList();
    }

    @Test
    void testMethodArgumentTypeMismatch() throws Exception {
        //given
        String id = "id";
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/galaxy/get/{id}", id)).andReturn().getResponse();
        //then
        verifyRestErrorResponse(response.getContentAsString(), ErrorCodeType.INVALID_REQUEST_PARAMETER);
        then(service).should(never()).get(any());
    }

    @Test
    void testObjectOptimisticLockingFailure() throws Exception {
        //given
        GalaxyDto dto = TestUtils.buildSampleGalaxyDtoForUpdate();
        Galaxy entity = modelMapper.map(dto, Galaxy.class);

        given(service.update(any())).willThrow(ObjectOptimisticLockingFailureException.class);
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/galaxy/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
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
