package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.common.test.AbstractSpringBootTest;
import com.example.universe.simulator.entityservice.common.abstractions.AbstractMockMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;

@AbstractSpringBootTest
@AutoConfigureMockMvc
@RecordApplicationEvents
abstract class AbstractIntegrationTest extends AbstractMockMvcTest {

    private static final RabbitMQContainer RABBITMQ_CONTAINER;
    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER;

    @Autowired
    protected ApplicationEvents applicationEvents;

    static {
        RABBITMQ_CONTAINER = new RabbitMQContainer("rabbitmq:3.9.13-management");
        POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:14.2");

        RABBITMQ_CONTAINER.start();
        POSTGRESQL_CONTAINER.start();
    }

    @DynamicPropertySource
    private static void addTestContainersProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", RABBITMQ_CONTAINER::getHost);
        registry.add("spring.rabbitmq.port", RABBITMQ_CONTAINER::getAmqpPort);
        registry.add("spring.rabbitmq.username", RABBITMQ_CONTAINER::getAdminUsername);
        registry.add("spring.rabbitmq.password", RABBITMQ_CONTAINER::getAdminPassword);

        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
    }
}
