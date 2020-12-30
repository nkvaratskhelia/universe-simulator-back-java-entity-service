package com.example.universe.simulator.entityservice.repositories;

import com.example.universe.simulator.entityservice.entities.Moon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MoonRepository extends JpaRepository<Moon, UUID> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, UUID id);

    boolean existsByPlanetId(UUID id);
}
