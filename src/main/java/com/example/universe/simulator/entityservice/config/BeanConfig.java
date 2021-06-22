package com.example.universe.simulator.entityservice.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;

@Configuration
public class BeanConfig {

    /**
     * AsyncTaskExecutor named applicationTaskExecutor is needed for Spring MVC async processing. Check the following pages for more info:
     * https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-task-execution-scheduling
     * https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-async-configuration-spring-mvc
     *
     * When more than 1 TaskExecutor beans are in the context, a bean named taskExecutor is needed for Sleuth. This is based on the following log message:
     * org.springframework.cloud.sleuth.autoconfig.instrument.async.TraceAsyncDefaultAutoConfiguration$DefaultAsyncConfigurerSupport: More than oneTaskExecutor
     * bean found within the context, and none is named 'taskExecutor'. Mark one of them as primary or name it 'taskExecutor' (possibly as an alias) in order to
     * use it for async processing: [applicationTaskExecutor, taskScheduler]. For backward compatibility, will fallback to the default, SimpleAsyncTaskExecutor
     * implementation.
     */
    @Bean({"taskExecutor", "applicationTaskExecutor"})
    AsyncTaskExecutor asyncTaskExecutor(TaskExecutorBuilder taskExecutorBuilder) {
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
