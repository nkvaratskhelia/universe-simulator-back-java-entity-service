package com.example.universe.simulator.entityservice.repositories;

import com.example.universe.simulator.entityservice.entities.Moon;

import java.util.UUID;

public interface MoonRepository extends SpaceEntityRepository<Moon> {

    boolean existsByPlanetId(UUID id);
}
