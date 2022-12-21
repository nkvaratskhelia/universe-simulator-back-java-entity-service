package com.example.universe.simulator.entityservice.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "app.scheduling", name = "enabled", havingValue = "true")
@EnableScheduling
class SchedulingConfig {}
