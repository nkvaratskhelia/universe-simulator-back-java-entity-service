package com.example.universe.simulator.entityservice.services;

import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.events.EventPublisher;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.repositories.MoonRepository;
import com.example.universe.simulator.entityservice.repositories.PlanetRepository;
import com.example.universe.simulator.entityservice.repositories.StarRepository;
import com.example.universe.simulator.entityservice.types.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlanetService extends SpaceEntityService<Planet> {

    private final PlanetRepository repository;
    private final StarRepository starRepository;
    private final MoonRepository moonRepository;
    private final EventPublisher eventPublisher;

    public Page<Planet> getList(Specification<Planet> specification, Pageable pageable) {
        Page<Planet> result = repository.findAll(specification, pageable);
        log.info("fetched [{}] record(s)", result.getNumberOfElements());

        return result;
    }

    public Planet get(UUID id) throws AppException {
        Optional<Planet> result = repository.findById(id);
        result.ifPresent(entity -> log.info("fetched [{}]", id));

        return result.orElseThrow(() -> new AppException(ErrorCodeType.NOT_FOUND_ENTITY));
    }

    @Transactional
    public Planet add(Planet entity) throws AppException {
        validate(entity, false, repository);
        Planet result = repository.save(entity);

        log.info("added [{}]", result.getId());
        eventPublisher.publishEvent(EventType.PLANET_ADD, result.getId().toString());

        return result;
    }

    @Transactional
    public Planet update(Planet entity) throws AppException {
        validate(entity, true, repository);
        Planet result = repository.save(entity);

        log.info("updated [{}]", entity.getId());
        eventPublisher.publishEvent(EventType.PLANET_UPDATE, entity.getId().toString());

        return result;
    }

    @Transactional
    public void delete(UUID id) throws AppException {
        if (moonRepository.existsByPlanetId(id)) {
            throw new AppException(ErrorCodeType.IN_USE);
        }

        repository.deleteById(id);

        log.info("deleted [{}]", id);
        eventPublisher.publishEvent(EventType.PLANET_DELETE, id.toString());
    }

    @Override
    void validateEntity(Planet entity, boolean isUpdate) throws AppException {
        if (!starRepository.existsById(entity.getStar().getId())) {
            throw new AppException(ErrorCodeType.NOT_FOUND_STAR);
        }
    }
}
