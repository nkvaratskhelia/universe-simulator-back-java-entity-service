package com.example.universe.simulator.entityservice.entities;

import lombok.Getter;
import lombok.Setter;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
