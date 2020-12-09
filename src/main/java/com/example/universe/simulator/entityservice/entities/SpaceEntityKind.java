package com.example.universe.simulator.entityservice.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class SpaceEntityKind {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String kind; // gas, firm, etc.

}
