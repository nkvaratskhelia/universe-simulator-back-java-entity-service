package com.example.universe.simulator.entityservice.events;

import com.example.universe.simulator.common.dtos.EventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventProcessor {

    private final RabbitTemplate rabbitTemplate;

    private final String eventQueue;

    public EventProcessor(RabbitTemplate rabbitTemplate,
                          @Value("${app.rabbitmq.event-queue}") String eventQueue) {
        this.rabbitTemplate = rabbitTemplate;
        this.eventQueue = eventQueue;
    }

    @EventListener
    @Async
    public void process(EventDto event) {
        log.info("received {}", event);
        rabbitTemplate.convertAndSend(eventQueue, event);
        log.info("processed {}", event);
    }
}
