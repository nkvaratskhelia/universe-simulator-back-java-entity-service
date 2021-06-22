package com.example.universe.simulator.entityservice.unit.events;

import com.example.universe.simulator.common.events.Event;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.events.EventPublisher;
import com.example.universe.simulator.entityservice.types.EventType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class EventPublisherTest {

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private EventPublisher eventPublisher;

    @Test
    void testProcess() {
        // given
        Event event = TestUtils.buildEvent();
        // when
        eventPublisher.publishEvent(EventType.valueOf(event.getType()), event.getData());
        // then
        then(applicationEventPublisher).should().publishEvent(event);
    }
}
