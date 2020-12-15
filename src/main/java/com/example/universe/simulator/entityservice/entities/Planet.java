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
public class Planet extends SpaceEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    private Star star;

    @OneToMany(mappedBy = "planet")
    private Set<Moon> moons;
}
