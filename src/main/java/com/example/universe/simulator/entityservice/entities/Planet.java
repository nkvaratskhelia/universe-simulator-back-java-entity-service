package com.example.universe.simulator.entityservice.entities;

import com.example.universe.simulator.entityservice.entities.abstraction.SpaceEntityInhabitable;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Planet extends SpaceEntityInhabitable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "planet_race",
            joinColumns = @JoinColumn(name = "planet_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "race_id", referencedColumnName = "id")
    )
    private Set<Race> races = new HashSet<>();

    // other planet related fields

}
