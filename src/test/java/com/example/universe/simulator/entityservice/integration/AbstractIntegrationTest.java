package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.common.abstractions.AbstractMockMvcTest;
import com.example.universe.simulator.entityservice.common.abstractions.AbstractSpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;

@AbstractSpringBootTest
@AutoConfigureMockMvc
abstract class AbstractIntegrationTest extends AbstractMockMvcTest {

    private static final RabbitMQContainer RABBITMQ_CONTAINER;

    static {
        RABBITMQ_CONTAINER = new RabbitMQContainer("rabbitmq:management");
        RABBITMQ_CONTAINER.start();
    }

    /**
     * For networking info in TestContainers, check the following url:
     * https://www.testcontainers.org/features/networking/
     */
    @DynamicPropertySource
    private static void addTestContainersProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", RABBITMQ_CONTAINER::getHost);
        registry.add("spring.rabbitmq.port", RABBITMQ_CONTAINER::getAmqpPort);
        registry.add("spring.rabbitmq.username", RABBITMQ_CONTAINER::getAdminUsername);
        registry.add("spring.rabbitmq.password", RABBITMQ_CONTAINER::getAdminPassword);
    }
}
