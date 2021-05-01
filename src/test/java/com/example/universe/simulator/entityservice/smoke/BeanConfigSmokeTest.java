package com.example.universe.simulator.entityservice.smoke;

import com.example.universe.simulator.entityservice.common.AbstractSpringBootTest;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.assertj.core.api.Assertions.assertThat;

@AbstractSpringBootTest
class BeanConfigSmokeTest {

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private AsyncTaskExecutor applicationTaskExecutor;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    void test() {
        //-----------------------------------taskExecutor-----------------------------------
        //given
        int numProcessors = Runtime.getRuntime().availableProcessors();
        //then
        assertThat(taskExecutor)
            .isNotNull()
            .isInstanceOf(ThreadPoolTaskExecutor.class);

        ThreadPoolTaskExecutor threadPoolTaskExecutor = (ThreadPoolTaskExecutor) taskExecutor;
        assertThat(threadPoolTaskExecutor.getCorePoolSize()).isEqualTo(numProcessors);
        assertThat(threadPoolTaskExecutor.getMaxPoolSize()).isEqualTo(numProcessors);

        //-----------------------------------applicationTaskExecutor-----------------------------------
        //then
        assertThat(applicationTaskExecutor).isSameAs(taskExecutor);

        //-----------------------------------modelMapper-----------------------------------
        //then
        assertThat(modelMapper).isNotNull();
    }
}
