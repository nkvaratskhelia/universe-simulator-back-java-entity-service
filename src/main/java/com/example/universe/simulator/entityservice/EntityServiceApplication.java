package com.example.universe.simulator.entityservice;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

import com.example.universe.simulator.common.config.CachingConfig;
import com.example.universe.simulator.common.config.RabbitMQConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@Import({CachingConfig.class, RabbitMQConfig.class})
@EnableAsync
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class EntityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EntityServiceApplication.class, args);
    }
}
