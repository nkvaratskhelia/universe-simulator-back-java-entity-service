package com.example.universe.simulator.entityservice;

import com.example.universe.simulator.common.config.RabbitMQConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@Import(RabbitMQConfig.class)
@EnableAsync
public class EntityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EntityServiceApplication.class, args);
    }
}
