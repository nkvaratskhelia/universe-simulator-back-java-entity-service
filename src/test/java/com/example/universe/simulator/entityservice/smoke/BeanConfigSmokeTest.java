package com.example.universe.simulator.entityservice.smoke;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.assertj.core.api.Assertions.assertThat;

class BeanConfigSmokeTest extends AbstractSmokeTest {

    @Autowired
    @Qualifier("applicationTaskExecutor")
    private ThreadPoolTaskExecutor applicationTaskExecutor;

    @Autowired
    @Qualifier("taskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    void test() {
        // -----------------------------------applicationTaskExecutor-----------------------------------

        // given
        int numProcessors = Runtime.getRuntime().availableProcessors();
        // then
        assertThat(applicationTaskExecutor).isNotNull();
        assertThat(applicationTaskExecutor.getCorePoolSize()).isEqualTo(numProcessors);
        assertThat(applicationTaskExecutor.getMaxPoolSize()).isEqualTo(numProcessors);

        // -----------------------------------taskExecutor-----------------------------------

        // then
        assertThat(taskExecutor).isSameAs(applicationTaskExecutor);

        // -----------------------------------modelMapper-----------------------------------

        // then
        assertThat(modelMapper).isNotNull();
    }
}
