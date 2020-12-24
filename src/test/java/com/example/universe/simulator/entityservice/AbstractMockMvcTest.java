package com.example.universe.simulator.entityservice;

import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.exception.RestErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
public abstract class AbstractMockMvcTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected void verifyRestErrorResponse(String responseContent, ErrorCodeType errorCode) throws JsonProcessingException {
        RestErrorResponse restErrorResponse = objectMapper.readValue(responseContent, RestErrorResponse.class);
        assertEquals(errorCode, restErrorResponse.getError());
        assertEquals(errorCode.getHttpStatus().value(), restErrorResponse.getStatus());
    }
}
