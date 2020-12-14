package com.example.universe.simulator.entityservice.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
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
