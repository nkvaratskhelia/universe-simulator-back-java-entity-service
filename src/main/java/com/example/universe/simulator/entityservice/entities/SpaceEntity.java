package com.example.universe.simulator.entityservice.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

/**
 * parent for all space objects
 */
@MappedSuperclass
@Getter @Setter
public abstract class SpaceEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Version
    private long version;
}
