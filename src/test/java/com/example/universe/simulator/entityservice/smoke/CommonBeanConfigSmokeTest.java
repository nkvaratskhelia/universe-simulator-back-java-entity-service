package com.example.universe.simulator.entityservice.smoke;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class CommonBeanConfigSmokeTest extends AbstractSmokeTest {

    @Autowired
    private ModelMapper modelMapper;

    @Test
    void test() {
        assertThat(modelMapper).isNotNull();
    }
}
