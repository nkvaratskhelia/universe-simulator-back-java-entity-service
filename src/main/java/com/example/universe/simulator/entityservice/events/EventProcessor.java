package com.example.universe.simulator.entityservice.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventProcessor {

    @EventListener
    @Async
    public void process(Event event) {
        log.info("processed {}", event);
    }
}
