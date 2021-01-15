package com.example.universe.simulator.entityservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

@NoRepositoryBean
public interface SpaceEntityRepository<T> extends JpaRepository<T, UUID>, JpaSpecificationExecutor<T> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, UUID id);
}
