package com.example.universe.simulator.entityservice.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class BeanConfig {

    //https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-async-configuration-spring-mvc
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
