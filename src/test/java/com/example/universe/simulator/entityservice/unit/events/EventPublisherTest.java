package com.example.universe.simulator.entityservice.unit.events;

import com.example.universe.simulator.common.dtos.EventDto;
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
    void testPublish() {
        // given
        EventDto event = TestUtils.buildEventDto();
        // when
        eventPublisher.publish(EventType.valueOf(event.getType()), event.getData());
        // then
        then(applicationEventPublisher).should().publishEvent(event);
    }
}
