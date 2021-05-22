package com.example.universe.simulator.entityservice.events;

import com.example.universe.simulator.entityservice.types.EventType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Getter @Setter
@Builder
@EqualsAndHashCode
@ToString
public class Event implements Serializable {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType type;

    @Column(nullable = false)
    private String data;

    @Column(nullable = false)
    @EqualsAndHashCode.Exclude
    private OffsetDateTime time;
}
