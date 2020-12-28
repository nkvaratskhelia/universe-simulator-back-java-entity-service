package com.example.universe.simulator.entityservice.repositories;

import com.example.universe.simulator.entityservice.entities.Star;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StarRepository extends JpaRepository<Star, UUID> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, UUID id);
}
