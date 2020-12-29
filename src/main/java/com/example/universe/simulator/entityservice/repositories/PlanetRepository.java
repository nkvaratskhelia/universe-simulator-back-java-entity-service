package com.example.universe.simulator.entityservice.repositories;

import com.example.universe.simulator.entityservice.entities.Planet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlanetRepository extends JpaRepository<Planet, UUID> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, UUID id);

    boolean existsByStarId(UUID id);
}
