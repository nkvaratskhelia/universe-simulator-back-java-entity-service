package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.common.abstractions.AbstractMockMvcTest;
import com.example.universe.simulator.entityservice.common.abstractions.AbstractSpringBootTest;
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

    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER;
    private static final RabbitMQContainer RABBITMQ_CONTAINER;

    @Autowired
    protected ApplicationEvents applicationEvents;

    static {
        POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:14.1");
        RABBITMQ_CONTAINER = new RabbitMQContainer("rabbitmq:3.9.13-management");

        RABBITMQ_CONTAINER.start();
        POSTGRESQL_CONTAINER.start();
    }

    /**
     * For networking info in TestContainers, check the following url:
     * https://www.testcontainers.org/features/networking/
     */
    @DynamicPropertySource
    private static void addTestContainersProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);

        registry.add("spring.rabbitmq.host", RABBITMQ_CONTAINER::getHost);
        registry.add("spring.rabbitmq.port", RABBITMQ_CONTAINER::getAmqpPort);
        registry.add("spring.rabbitmq.username", RABBITMQ_CONTAINER::getAdminUsername);
        registry.add("spring.rabbitmq.password", RABBITMQ_CONTAINER::getAdminPassword);
    }
}
