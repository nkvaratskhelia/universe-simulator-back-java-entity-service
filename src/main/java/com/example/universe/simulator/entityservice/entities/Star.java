package com.example.universe.simulator.entityservice.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter @Setter
public class Star extends SpaceEntity {

    @ManyToOne
    @JoinColumn(name = "galaxy_id", referencedColumnName = "id", nullable = false)
    private Galaxy galaxy;

    @OneToMany(mappedBy = "star")
    private Set<Planet> planets;

}
