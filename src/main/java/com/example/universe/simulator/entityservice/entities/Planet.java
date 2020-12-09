package com.example.universe.simulator.entityservice.entities;

import com.example.universe.simulator.entityservice.entities.abstraction.SpaceEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Planet extends SpaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long population;

    @ManyToMany
    @JoinTable(
            name = "planet_race",
            joinColumns = @JoinColumn(name = "planet_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "race_id", referencedColumnName = "id")
    )
    private Set<Race> races = new HashSet<>();

    // other fields

}
