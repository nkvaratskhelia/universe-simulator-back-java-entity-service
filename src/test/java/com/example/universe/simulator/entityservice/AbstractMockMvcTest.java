package com.example.universe.simulator.entityservice;

import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.exception.RestErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
public abstract class AbstractMockMvcTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected final void verifyOkStatus(int status) {
        assertThat(status).isEqualTo(HttpStatus.OK.value());
    }

    protected final void verifyErrorResponse(String responseContent, ErrorCodeType errorCode) throws JsonProcessingException {
        RestErrorResponse restErrorResponse = objectMapper.readValue(responseContent, RestErrorResponse.class);

        assertThat(restErrorResponse.getError()).isEqualTo(errorCode);
        assertThat(restErrorResponse.getStatus()).isEqualTo(errorCode.getHttpStatus().value());
    }
}
