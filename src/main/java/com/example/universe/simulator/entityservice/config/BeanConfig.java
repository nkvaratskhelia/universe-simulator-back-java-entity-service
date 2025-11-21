package com.example.universe.simulator.entityservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration(proxyBeanMethods = false)
class BeanConfig {

    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }
}
