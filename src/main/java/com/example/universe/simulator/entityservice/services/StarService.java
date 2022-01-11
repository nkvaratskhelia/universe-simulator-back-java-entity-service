package com.example.universe.simulator.entityservice.services;

import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.events.EventPublisher;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.repositories.GalaxyRepository;
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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StarService extends SpaceEntityService<Star> {

    private final StarRepository repository;
    private final GalaxyRepository galaxyRepository;
    private final PlanetRepository planetRepository;
    private final EventPublisher eventPublisher;

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
        Star result = repository.save(entity);

        eventPublisher.publish(EventType.STAR_ADD, result.getId());

        return result;
    }

    @Transactional
    public Star update(Star entity) throws AppException {
        validate(entity, true, repository);
        Star result = repository.save(entity);

        eventPublisher.publish(EventType.STAR_UPDATE, entity.getId());

        return result;
    }

    @Transactional
    public void delete(UUID id) throws AppException {
        if (planetRepository.existsByStarId(id)) {
            throw new AppException(ErrorCodeType.IN_USE);
        }
        repository.deleteById(id);

        eventPublisher.publish(EventType.STAR_DELETE, id);
    }

    @Override
    void validateEntity(Star entity, boolean isUpdate) throws AppException {
        if (!galaxyRepository.existsById(entity.getGalaxy().getId())) {
            throw new AppException(ErrorCodeType.NOT_FOUND_GALAXY);
        }
    }
}
