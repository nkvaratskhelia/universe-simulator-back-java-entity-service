package com.example.universe.simulator.entityservice.unit.events;

import com.example.universe.simulator.common.events.Event;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.events.EventProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class EventProcessorTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private EventProcessor eventProcessor;

    @Test
    void testProcess() {
        // given
        Event event = TestUtils.buildEvent();
        // when
        eventProcessor.process(event);
        // then
        then(rabbitTemplate).should().convertAndSend(event);
    }
}
