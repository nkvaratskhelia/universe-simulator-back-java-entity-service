package com.example.universe.simulator.entityservice.services;

import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.events.EventPublisher;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.repositories.GalaxyRepository;
import com.example.universe.simulator.entityservice.repositories.StarRepository;
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
public class GalaxyService extends SpaceEntityService<Galaxy> {

    private final GalaxyRepository repository;
    private final StarRepository starRepository;
    private final EventPublisher eventPublisher;

    public Page<Galaxy> getList(Specification<Galaxy> specification, Pageable pageable) {
        return repository.findAll(specification, pageable);
    }

    public Galaxy get(UUID id) throws AppException {
        return repository.findById(id)
            .orElseThrow(() -> new AppException(ErrorCodeType.NOT_FOUND_ENTITY));
    }

    @Transactional
    public Galaxy add(Galaxy entity) throws AppException {
        validate(entity, false, repository);
        Galaxy result = repository.save(entity);

        eventPublisher.publishEvent(EventType.GALAXY_ADD, result.getId());

        return result;
    }

    @Transactional
    public Galaxy update(Galaxy entity) throws AppException {
        validate(entity, true, repository);
        Galaxy result = repository.save(entity);

        eventPublisher.publishEvent(EventType.GALAXY_UPDATE, entity.getId());

        return result;
    }

    @Transactional
    public void delete(UUID id) throws AppException {
        if (starRepository.existsByGalaxyId(id)) {
            throw new AppException(ErrorCodeType.IN_USE);
        }
        repository.deleteById(id);

        eventPublisher.publishEvent(EventType.GALAXY_DELETE, id);
    }

    @Override
    void validateEntity(Galaxy entity, boolean isUpdate) {
        // there is no entity-specific validation yet
    }
}
