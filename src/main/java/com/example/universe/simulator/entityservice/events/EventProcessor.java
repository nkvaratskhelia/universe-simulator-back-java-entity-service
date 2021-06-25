package com.example.universe.simulator.entityservice.events;

import com.example.universe.simulator.common.events.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventProcessor {

    private final RabbitTemplate rabbitTemplate;

    @EventListener
    @Async
    public void process(Event event) {
        rabbitTemplate.convertAndSend(event);
        log.info("processed {}", event);
    }
}
