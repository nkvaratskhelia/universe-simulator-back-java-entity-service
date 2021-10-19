package com.example.universe.simulator.entityservice.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter @Setter
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public abstract class SpaceEntity extends AbstractEntity {

    @Column(nullable = false, unique = true)
    private String name;
}
