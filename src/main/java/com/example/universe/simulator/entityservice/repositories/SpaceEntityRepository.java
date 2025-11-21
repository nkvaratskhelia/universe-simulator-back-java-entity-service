package com.example.universe.simulator.entityservice.repositories;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

@NoRepositoryBean
public interface SpaceEntityRepository<T> extends JpaRepository<@NonNull T, @NonNull UUID>, JpaSpecificationExecutor<@NonNull T> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, UUID id);
}
