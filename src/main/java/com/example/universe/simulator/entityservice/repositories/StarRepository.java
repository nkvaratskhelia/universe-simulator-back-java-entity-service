package com.example.universe.simulator.entityservice.repositories;

import com.example.universe.simulator.entityservice.entities.Star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface StarRepository extends JpaRepository<Star, UUID>, JpaSpecificationExecutor<Star> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, UUID id);

    boolean existsByGalaxyId(UUID id);
}
