package com.example.universe.simulator.entityservice.dtos;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode
@ToString
public abstract class SpaceEntityDto {

    private UUID id;
    private Long version;
    private String name;
}
