package com.example.universe.simulator.entityservice.common.abstractions;

import com.example.universe.simulator.entityservice.exception.ErrorResponse;
import com.example.universe.simulator.entityservice.types.ErrorCodeType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;

public abstract class AbstractMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected final void verifyOkStatus(int status) {
        assertThat(status).isEqualTo(HttpStatus.OK.value());
    }

    protected final void verifyErrorResponse(MockHttpServletResponse response, ErrorCodeType errorCode)
        throws JsonProcessingException, UnsupportedEncodingException {
        assertThat(response.getStatus()).isEqualTo(errorCode.getHttpStatus().value());

        ErrorResponse errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorResponse.class);
        assertThat(errorResponse.getError()).isEqualTo(errorCode);
    }

    //handles sync and async requests
    //for async requests, see https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#spring-mvc-test-async-requests
    protected final MockHttpServletResponse performRequest(RequestBuilder requestBuilder) throws Exception {
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        return mvcResult.getRequest().isAsyncStarted()
            ? mockMvc.perform(asyncDispatch(mvcResult)).andReturn().getResponse()
            : mvcResult.getResponse();
    }
}
