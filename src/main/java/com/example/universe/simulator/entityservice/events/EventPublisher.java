package com.example.universe.simulator.entityservice.events;

import com.example.universe.simulator.common.events.Event;
import com.example.universe.simulator.entityservice.types.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(EventType type, String data) {
        var event = Event.builder()
            .type(type.toString())
            .data(data)
            .time(OffsetDateTime.now())
            .build();
        applicationEventPublisher.publishEvent(event);

        log.info("published {}", event);
    }
}
