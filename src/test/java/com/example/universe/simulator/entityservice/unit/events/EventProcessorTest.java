package com.example.universe.simulator.entityservice.unit.events;

import com.example.universe.simulator.common.dtos.EventDto;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.events.EventProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.Clock;

import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class EventProcessorTest {

    private static final String EVENT_QUEUE = "eventQueue";

    @Mock
    private RabbitTemplate rabbitTemplate;

    private EventProcessor eventProcessor;

    @BeforeEach
    void init() {
        eventProcessor = new EventProcessor(rabbitTemplate, EVENT_QUEUE);
    }

    @Test
    void testProcess() {
        // given
        EventDto event = TestUtils.buildEventDto(Clock.systemDefaultZone());
        // when
        eventProcessor.process(event);
        // then
        then(rabbitTemplate).should().convertAndSend(EVENT_QUEUE, event);
    }
}
