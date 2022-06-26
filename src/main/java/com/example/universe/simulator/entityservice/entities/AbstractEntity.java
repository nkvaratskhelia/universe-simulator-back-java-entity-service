package com.example.universe.simulator.entityservice.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.UUID;

@MappedSuperclass
@Getter @Setter
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode
abstract class AbstractEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Version
    private long version;
}
