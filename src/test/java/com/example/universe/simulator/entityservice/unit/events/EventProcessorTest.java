package com.example.universe.simulator.entityservice.unit.events;

import com.example.universe.simulator.entityservice.entities.Event;
import com.example.universe.simulator.entityservice.events.EventProcessor;
import com.example.universe.simulator.entityservice.repositories.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class EventProcessorTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventProcessor eventProcessor;

    @Test
    void testProcess() {
        //given
        Event event = Event.builder().build();
        //when
        eventProcessor.process(event);
        //then
        then(eventRepository).should().save(event);
    }
}
