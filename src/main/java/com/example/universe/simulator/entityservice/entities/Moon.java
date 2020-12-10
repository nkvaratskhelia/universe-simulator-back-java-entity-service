package com.example.universe.simulator.entityservice.entities;

import com.example.universe.simulator.entityservice.entities.abstraction.SpaceEntityInhabitable;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Moon extends SpaceEntityInhabitable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "moon_race",
            joinColumns = @JoinColumn(name = "moon_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "race_id", referencedColumnName = "id")
    )
    private Set<Race> races = new HashSet<>();

    // other moon related fields

}
