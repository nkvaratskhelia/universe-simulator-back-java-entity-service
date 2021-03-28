package com.example.universe.simulator.entityservice.events;

import com.example.universe.simulator.entityservice.entities.Event;
import com.example.universe.simulator.entityservice.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventProcessor {

    private final EventRepository eventRepository;

    @EventListener
    @Async
    public void process(Event event) {
        eventRepository.save(event);
        log.info("processed {}", event);
    }
}
