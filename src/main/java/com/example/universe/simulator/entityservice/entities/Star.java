package com.example.universe.simulator.entityservice.entities;

import com.example.universe.simulator.entityservice.entities.abstraction.SpaceEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Star extends SpaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // other star specific fields

}
