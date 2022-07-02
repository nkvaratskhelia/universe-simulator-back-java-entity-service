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
    private Clock clock;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    void test() {
        // applicationTaskExecutor

        int numProcessors = Runtime.getRuntime().availableProcessors();

        assertThat(applicationTaskExecutor).isNotNull();
        assertThat(applicationTaskExecutor.getCorePoolSize()).isEqualTo(numProcessors);
        assertThat(applicationTaskExecutor.getMaxPoolSize()).isEqualTo(numProcessors);

        // taskExecutor
        assertThat(taskExecutor).isSameAs(applicationTaskExecutor);

        // clock
        assertThat(clock).isEqualTo(Clock.systemDefaultZone());

        // modelMapper
        assertThat(modelMapper).isNotNull();
    }
}
