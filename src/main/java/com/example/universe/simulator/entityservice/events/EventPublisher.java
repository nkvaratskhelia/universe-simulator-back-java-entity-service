package com.example.universe.simulator.entityservice.events;

import com.example.universe.simulator.common.dtos.EventDto;
import com.example.universe.simulator.entityservice.types.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final Clock clock;

    public void publish(EventType type, Object data) {
        var event = EventDto.builder()
            .type(type.toString())
            .data(data.toString())
            .time(OffsetDateTime.now(clock))
            .build();
        applicationEventPublisher.publishEvent(event);

        log.info("published {}", event);
    }
}
