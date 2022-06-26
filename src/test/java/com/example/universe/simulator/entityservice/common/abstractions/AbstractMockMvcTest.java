package com.example.universe.simulator.entityservice.common.abstractions;

import com.example.universe.simulator.common.dtos.ErrorDto;
import com.example.universe.simulator.entityservice.types.ErrorCodeType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;

public abstract class AbstractMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    /*
     * Handles sync and async requests. For async requests, see
     * https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#spring-mvc-test-async-requests
     */
    protected final MockHttpServletResponse performRequest(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        return mvcResult.getRequest().isAsyncStarted()
               ? mockMvc.perform(asyncDispatch(mvcResult)).andReturn().getResponse()
               : mvcResult.getResponse();
    }

    protected final MockHttpServletResponse performRequestWithBody(MockHttpServletRequestBuilder requestBuilder, Object body) throws Exception {
        return performRequest(
            requestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
        );
    }

    protected final <T> T readResponse(MockHttpServletResponse response, Class<T> responseClass)
        throws JsonProcessingException, UnsupportedEncodingException {
        return objectMapper.readValue(response.getContentAsString(), responseClass);
    }

    protected final void verifyErrorResponse(MockHttpServletResponse response, ErrorCodeType errorCode)
        throws JsonProcessingException, UnsupportedEncodingException {
        assertThat(response.getStatus()).isEqualTo(errorCode.getHttpStatus().value());

        ErrorDto errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorDto.class);
        assertThat(errorResponse.errorCode()).isEqualTo(errorCode.toString());
    }
}
