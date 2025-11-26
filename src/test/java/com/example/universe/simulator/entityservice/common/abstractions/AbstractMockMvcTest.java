package com.example.universe.simulator.entityservice.common.abstractions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;

import com.example.universe.simulator.entityservice.exception.RestExceptionHandler;
import com.example.universe.simulator.entityservice.types.ErrorCodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import tools.jackson.databind.json.JsonMapper;

import java.io.UnsupportedEncodingException;

public abstract class AbstractMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected JsonMapper jsonMapper;

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
                .content(jsonMapper.writeValueAsString(body))
        );
    }

    protected final <T> T readResponse(MockHttpServletResponse response, Class<T> responseClass) throws UnsupportedEncodingException {
        return jsonMapper.readValue(response.getContentAsString(), responseClass);
    }

    protected final void verifyErrorResponse(MockHttpServletResponse response, ErrorCodeType errorCode) throws UnsupportedEncodingException {
        assertThat(response.getStatus()).isEqualTo(errorCode.getHttpStatus().value());

        ProblemDetail errorResponse = jsonMapper.readValue(response.getContentAsString(), ProblemDetail.class);
        assertThat(errorResponse.getDetail()).isEqualTo(errorCode.toString());
        assertThat(errorResponse.getProperties())
            .isNotNull()
            .extractingByKey(RestExceptionHandler.TIMESTAMP_PROPERTY)
            .isNotNull();
    }
}
