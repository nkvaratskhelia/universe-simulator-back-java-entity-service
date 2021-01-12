package com.example.universe.simulator.entityservice.repositories;

import com.example.universe.simulator.entityservice.entities.Moon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface MoonRepository extends JpaRepository<Moon, UUID>, JpaSpecificationExecutor<Moon> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, UUID id);

    boolean existsByPlanetId(UUID id);
}
