package com.example.universe.simulator.entityservice.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter @Setter
public class Moon extends SpaceEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    private Planet planet;
}
