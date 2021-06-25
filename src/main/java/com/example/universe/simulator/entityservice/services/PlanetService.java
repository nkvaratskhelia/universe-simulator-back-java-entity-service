package com.example.universe.simulator.entityservice.services;

import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.events.EventPublisher;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.repositories.MoonRepository;
import com.example.universe.simulator.entityservice.repositories.PlanetRepository;
import com.example.universe.simulator.entityservice.repositories.StarRepository;
import com.example.universe.simulator.entityservice.types.ErrorCodeType;
import com.example.universe.simulator.entityservice.types.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlanetService extends SpaceEntityService<Planet> {

    private final PlanetRepository repository;
    private final StarRepository starRepository;
    private final MoonRepository moonRepository;
    private final EventPublisher eventPublisher;

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
        Planet result = repository.save(entity);

        eventPublisher.publish(EventType.PLANET_ADD, result.getId());

        return result;
    }

    @Transactional
    public Planet update(Planet entity) throws AppException {
        validate(entity, true, repository);
        Planet result = repository.save(entity);

        eventPublisher.publish(EventType.PLANET_UPDATE, entity.getId());

        return result;
    }

    @Transactional
    public void delete(UUID id) throws AppException {
        if (moonRepository.existsByPlanetId(id)) {
            throw new AppException(ErrorCodeType.IN_USE);
        }
        repository.deleteById(id);

        eventPublisher.publish(EventType.PLANET_DELETE, id);
    }

    @Override
    void validateEntity(Planet entity, boolean isUpdate) throws AppException {
        if (!starRepository.existsById(entity.getStar().getId())) {
            throw new AppException(ErrorCodeType.NOT_FOUND_STAR);
        }
    }
}
