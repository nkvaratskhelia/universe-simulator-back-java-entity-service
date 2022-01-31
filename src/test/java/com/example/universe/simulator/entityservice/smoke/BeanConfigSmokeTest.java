package com.example.universe.simulator.entityservice.smoke;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Clock;

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

    @Autowired
    private Clock clock;

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

        // -----------------------------------clock-----------------------------------

        // then
        assertThat(clock).isEqualTo(Clock.systemDefaultZone());
    }
}
