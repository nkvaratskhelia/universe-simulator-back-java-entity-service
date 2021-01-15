package com.example.universe.simulator.entityservice.services;

import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.repositories.GalaxyRepository;
import com.example.universe.simulator.entityservice.repositories.PlanetRepository;
import com.example.universe.simulator.entityservice.repositories.StarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StarService extends SpaceEntityService<Star> {

    private final StarRepository repository;
    private final GalaxyRepository galaxyRepository;
    private final PlanetRepository planetRepository;

    public Page<Star> getList(Specification<Star> specification, Pageable pageable) {
        return repository.findAll(specification, pageable);
    }

    public Star get(UUID id) throws AppException {
        return repository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCodeType.NOT_FOUND_ENTITY));
    }

    @Transactional
    public Star add(Star entity) throws AppException {
        validate(entity, false, repository);
        return repository.save(entity);
    }

    @Transactional
    public Star update(Star entity) throws AppException {
        validate(entity, true, repository);
        return repository.save(entity);
    }

    @Transactional
    public void delete(UUID id) throws AppException {
        if (planetRepository.existsByStarId(id)) {
            throw new AppException(ErrorCodeType.IN_USE);
        }

        repository.deleteById(id);
    }

    @Override
    void validateEntity(Star entity, boolean isUpdate) throws AppException {
        if (!galaxyRepository.existsById(entity.getGalaxy().getId())) {
            throw new AppException(ErrorCodeType.NOT_FOUND_GALAXY);
        }
    }
}
