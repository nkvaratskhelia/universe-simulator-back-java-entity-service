package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.common.dtos.EventDto;
import com.example.universe.simulator.common.test.AbstractSpringBootTest;
import com.example.universe.simulator.entityservice.common.abstractions.AbstractMockMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@AbstractSpringBootTest
@AutoConfigureMockMvc
@RecordApplicationEvents
abstract class AbstractIntegrationTest extends AbstractMockMvcTest {

    private static final RabbitMQContainer RABBITMQ_CONTAINER;
    private static final GenericContainer<?> REDIS_CONTAINER;
    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER;

    @Autowired
    private ApplicationEvents applicationEvents;

    @Autowired
    protected CacheManager cacheManager;

    static {
        RABBITMQ_CONTAINER = new RabbitMQContainer("rabbitmq:3.12.0-management");
        REDIS_CONTAINER = new GenericContainer<>("redis:7.0.11").withExposedPorts(6379);
        POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:15.3");

        Startables.deepStart(RABBITMQ_CONTAINER, REDIS_CONTAINER, POSTGRESQL_CONTAINER).join();
    }

    @DynamicPropertySource
    private static void addTestContainersProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", RABBITMQ_CONTAINER::getHost);
        registry.add("spring.rabbitmq.port", RABBITMQ_CONTAINER::getAmqpPort);
        registry.add("spring.rabbitmq.username", RABBITMQ_CONTAINER::getAdminUsername);
        registry.add("spring.rabbitmq.password", RABBITMQ_CONTAINER::getAdminPassword);

        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", REDIS_CONTAINER::getFirstMappedPort);

        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
    }

    protected final void verifyEventsByType(Map<String, Long> expected) {
        Map<String, Long> eventsByType = applicationEvents.stream(EventDto.class)
            .collect(Collectors.groupingBy(EventDto::type, Collectors.counting()));
        assertThat(eventsByType).isEqualTo(expected);
    }
}
