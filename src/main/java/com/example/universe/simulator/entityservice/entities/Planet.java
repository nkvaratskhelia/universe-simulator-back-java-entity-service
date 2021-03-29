package com.example.universe.simulator.entityservice.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter @Setter
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Planet extends SpaceEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    @EqualsAndHashCode.Exclude
    private Star star;
}
