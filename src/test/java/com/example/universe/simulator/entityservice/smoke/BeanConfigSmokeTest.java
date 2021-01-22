package com.example.universe.simulator.entityservice.smoke;

import com.example.universe.simulator.entityservice.common.AbstractSpringBootTest;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.assertj.core.api.Assertions.assertThat;

@AbstractSpringBootTest
class BeanConfigSmokeTest {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    @Qualifier("applicationTaskExecutor")
    private AsyncTaskExecutor asyncTaskExecutor;

    @Test
    void test() {
        //-----------------------------------modelMapper-----------------------------------
        //then
        assertThat(modelMapper).isNotNull();

        //-----------------------------------asyncTaskExecutor-----------------------------------
        //given
        int numProcessors = Runtime.getRuntime().availableProcessors();
        //then
        assertThat(asyncTaskExecutor).isNotNull();
        assertThat(asyncTaskExecutor).isInstanceOf(ThreadPoolTaskExecutor.class);

        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) asyncTaskExecutor;
        assertThat(executor.getCorePoolSize()).isEqualTo(numProcessors);
        assertThat(executor.getMaxPoolSize()).isEqualTo(numProcessors);
    }
}
