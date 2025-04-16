package com.example.universe.simulator.entityservice.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.universe.simulator.common.dtos.EventDto;
import com.example.universe.simulator.common.test.AbstractSpringBootTest;
import com.example.universe.simulator.entityservice.common.abstractions.AbstractMockMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Collectors;

@AbstractSpringBootTest
@AutoConfigureMockMvc
@RecordApplicationEvents
abstract class AbstractIntegrationTest extends AbstractMockMvcTest {

    @ServiceConnection
    private static final RabbitMQContainer RABBITMQ_CONTAINER;

    @ServiceConnection
    private static final GenericContainer<?> REDIS_CONTAINER;

    @ServiceConnection
    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER;

    @Autowired
    private ApplicationEvents applicationEvents;

    @Autowired
    protected CacheManager cacheManager;

    static {
        RABBITMQ_CONTAINER = new RabbitMQContainer("rabbitmq:4.0.8-management");
        REDIS_CONTAINER = new GenericContainer<>("redis:7.4.2").withExposedPorts(6379);
        POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:17.4");

        Startables.deepStart(RABBITMQ_CONTAINER, REDIS_CONTAINER, POSTGRESQL_CONTAINER).join();
    }

    protected final void verifyEventsByType(Map<String, Long> expected) {
        Map<String, Long> eventsByType = applicationEvents.stream(EventDto.class)
            .collect(Collectors.groupingBy(EventDto::type, Collectors.counting()));
        assertThat(eventsByType).isEqualTo(expected);
    }
}
