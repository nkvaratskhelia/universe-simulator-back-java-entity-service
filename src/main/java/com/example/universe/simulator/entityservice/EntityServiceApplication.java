package com.example.universe.simulator.entityservice;

import com.example.universe.simulator.common.config.CommonProperties;
import com.example.universe.simulator.common.config.RabbitMQConfig;
import com.example.universe.simulator.common.config.RabbitMQProperties;
import com.example.universe.simulator.entityservice.config.SchedulingProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableConfigurationProperties({CommonProperties.class, RabbitMQProperties.class, SchedulingProperties.class})
@Import(RabbitMQConfig.class)
@EnableAsync
public class EntityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EntityServiceApplication.class, args);
    }
}
