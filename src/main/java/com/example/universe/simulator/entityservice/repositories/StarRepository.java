package com.example.universe.simulator.entityservice.repositories;

import com.example.universe.simulator.entityservice.entities.Star;

import java.util.UUID;

public interface StarRepository extends SpaceEntityRepository<Star> {

    boolean existsByGalaxyId(UUID id);
}
