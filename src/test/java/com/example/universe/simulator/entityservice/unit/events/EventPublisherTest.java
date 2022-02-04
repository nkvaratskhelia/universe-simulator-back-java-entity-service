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

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class EventPublisherTest {

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private Clock clock;

    @InjectMocks
    private EventPublisher eventPublisher;

    @Test
    void testPublish() {
        // given
        Clock fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        EventDto event = TestUtils.buildEventDto(fixedClock);

        given(clock.instant()).willReturn(fixedClock.instant());
        given(clock.getZone()).willReturn(fixedClock.getZone());
        // when
        eventPublisher.publish(EventType.valueOf(event.type()), event.data());
        // then
        then(applicationEventPublisher).should().publishEvent(event);
    }
}
