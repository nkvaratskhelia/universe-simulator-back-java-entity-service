package com.example.universe.simulator.entityservice.services;

import com.example.universe.simulator.entityservice.entities.Moon;
import com.example.universe.simulator.entityservice.events.EventPublisher;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.repositories.MoonRepository;
import com.example.universe.simulator.entityservice.repositories.PlanetRepository;
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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MoonService extends SpaceEntityService<Moon> {

    private final MoonRepository repository;
    private final PlanetRepository planetRepository;
    private final EventPublisher eventPublisher;

    public Page<Moon> getList(Specification<Moon> specification, Pageable pageable) {
        return repository.findAll(specification, pageable);
    }

    public Moon get(UUID id) throws AppException {
        return repository.findById(id)
            .orElseThrow(() -> new AppException(ErrorCodeType.NOT_FOUND_ENTITY));
    }

    @Transactional
    public Moon add(Moon entity) throws AppException {
        validate(entity, false, repository);
        Moon result = repository.save(entity);

        eventPublisher.publish(EventType.MOON_ADD, result.getId());

        return result;
    }

    @Transactional
    public Moon update(Moon entity) throws AppException {
        validate(entity, true, repository);
        Moon result = repository.save(entity);

        eventPublisher.publish(EventType.MOON_UPDATE, entity.getId());

        return result;
    }

    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);

        eventPublisher.publish(EventType.MOON_DELETE, id);
    }

    @Override
    void validateEntity(Moon entity, boolean isUpdate) throws AppException {
        if (!planetRepository.existsById(entity.getPlanet().getId())) {
            throw new AppException(ErrorCodeType.NOT_FOUND_PLANET);
        }
    }
}
