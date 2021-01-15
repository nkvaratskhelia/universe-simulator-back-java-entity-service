package com.example.universe.simulator.entityservice.repositories;

import com.example.universe.simulator.entityservice.entities.Planet;

import java.util.UUID;

public interface PlanetRepository extends SpaceEntityRepository<Planet> {

    boolean existsByStarId(UUID id);
}
