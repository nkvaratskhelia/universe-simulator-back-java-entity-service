package com.example.universe.simulator.entityservice.common.abstractions;

import com.example.universe.simulator.common.test.AbstractTest;
import com.example.universe.simulator.entityservice.config.BeanConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;

@AbstractTest
@Import(BeanConfig.class)
public abstract class AbstractWebMvcTest extends AbstractMockMvcTest {

    @Autowired
    protected ModelMapper modelMapper;

    protected final void verifyOkStatus(int status) {
        assertThat(status).isEqualTo(HttpStatus.OK.value());
    }

    protected final void verifySuccessfulResponse(MockHttpServletResponse response, Object expected)
        throws JsonProcessingException, UnsupportedEncodingException {
        verifyOkStatus(response.getStatus());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(expected));
    }
}
