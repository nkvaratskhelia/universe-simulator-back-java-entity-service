package com.example.universe.simulator.entityservice.entities.abstraction;

import com.example.universe.simulator.entityservice.entities.SpaceEntityKind;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

/**
 * parent for all space objects
 */
@MappedSuperclass
@Data
public abstract class SpaceEntity {

    @Column(nullable = false)
    private Double mass;

    @Column(nullable = false)
    private Double radius;

    @Column(nullable = false)
    private Double orbitRadius;

    @Column(nullable = false)
    private Double orbitSpeed;

    @OneToOne
    @JoinColumn(name = "kind_id", referencedColumnName = "id", nullable = false)
    private SpaceEntityKind spaceEntityKind;

    @Column(nullable = false)
    private Long age;

}
