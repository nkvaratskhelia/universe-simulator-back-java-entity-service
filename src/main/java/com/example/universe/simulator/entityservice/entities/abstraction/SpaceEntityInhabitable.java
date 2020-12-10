package com.example.universe.simulator.entityservice.entities.abstraction;

import lombok.Data;

import javax.persistence.MappedSuperclass;

/**
 * parent for all planets and moons
 */
@MappedSuperclass
@Data
public abstract class SpaceEntityInhabitable extends SpaceEntity {

    private Long population;

}
