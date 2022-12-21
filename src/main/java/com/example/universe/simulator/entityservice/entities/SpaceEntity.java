package com.example.universe.simulator.entityservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@MappedSuperclass
@Getter @Setter
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode
public abstract class SpaceEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Version
    private long version;

    @Column(nullable = false, unique = true)
    private String name;
}
