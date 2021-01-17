package com.example.universe.simulator.entityservice.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class BeanConfig {

    @Bean
    AsyncTaskExecutor applicationTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        int numProcessors = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(numProcessors);
        executor.setMaxPoolSize(numProcessors);

        return executor;
    }

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
