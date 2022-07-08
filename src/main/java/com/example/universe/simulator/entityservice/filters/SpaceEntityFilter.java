package com.example.universe.simulator.entityservice.filters;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@EqualsAndHashCode
@ToString
public abstract class SpaceEntityFilter {

    private String name;
}
