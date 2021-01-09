package com.example.universe.simulator.entityservice.repositories;

import com.example.universe.simulator.entityservice.entities.Galaxy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface GalaxyRepository extends JpaRepository<Galaxy, UUID>, JpaSpecificationExecutor<Galaxy> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, UUID id);
}
