package com.example.universe.simulator.entityservice.services;

import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.repositories.MoonRepository;
import com.example.universe.simulator.entityservice.repositories.PlanetRepository;
import com.example.universe.simulator.entityservice.repositories.StarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlanetService extends SpaceEntityService<Planet> {

    private final PlanetRepository repository;
    private final StarRepository starRepository;
    private final MoonRepository moonRepository;

    public Page<Planet> getList(Specification<Planet> specification, Pageable pageable) {
        return repository.findAll(specification, pageable);
    }

    public Planet get(UUID id) throws AppException {
        return repository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCodeType.NOT_FOUND_ENTITY));
    }

    @Transactional
    public Planet add(Planet entity) throws AppException {
        validate(entity, false, repository);
        return repository.save(entity);
    }

    @Transactional
    public Planet update(Planet entity) throws AppException {
        validate(entity, true, repository);
        return repository.save(entity);
    }

    @Transactional
    public void delete(UUID id) throws AppException {
        if (moonRepository.existsByPlanetId(id)) {
            throw new AppException(ErrorCodeType.IN_USE);
        }

        repository.deleteById(id);
    }

    @Override
    void validateEntity(Planet entity, boolean isUpdate) throws AppException {
        if (!starRepository.existsById(entity.getStar().getId())) {
            throw new AppException(ErrorCodeType.NOT_FOUND_STAR);
        }
    }
}
