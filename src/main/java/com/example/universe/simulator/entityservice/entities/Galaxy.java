package com.example.universe.simulator.entityservice.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Getter @Setter
public class Galaxy extends SpaceEntity {

    @OneToMany(mappedBy = "galaxy")
    private Set<Star> stars;
}
