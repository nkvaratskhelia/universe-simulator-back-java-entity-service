package com.example.universe.simulator.entityservice.entities.abstraction;

import com.example.universe.simulator.entityservice.entities.SpaceEntityKind;
import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

/**
 * general parent for all space objects
 */
@MappedSuperclass
@Data
public abstract class SpaceEntity {

    private Double mass;

    private Double radius;

    private Double orbitRadius;

    private Double orbitSpeed;

    @OneToOne
    @JoinColumn(name = "kind_id", referencedColumnName = "id")
    private SpaceEntityKind spaceEntityKind;

    private Long age;

}
