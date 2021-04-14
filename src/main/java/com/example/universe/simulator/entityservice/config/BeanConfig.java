package com.example.universe.simulator.entityservice.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;

@Configuration
public class BeanConfig {

    //https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-async-configuration-spring-mvc
    //https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-task-execution-scheduling
    @Bean
    AsyncTaskExecutor applicationTaskExecutor(TaskExecutorBuilder taskExecutorBuilder) {
        int numProcessors = Runtime.getRuntime().availableProcessors();
        return taskExecutorBuilder
            .corePoolSize(numProcessors)
            .maxPoolSize(numProcessors)
            .build();
    }

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
