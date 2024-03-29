package com.example.universe.simulator.entityservice.services;

import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.events.EventPublisher;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.repositories.GalaxyRepository;
import com.example.universe.simulator.entityservice.repositories.StarRepository;
import com.example.universe.simulator.entityservice.types.ErrorCodeType;
import com.example.universe.simulator.entityservice.types.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@CacheConfig(cacheNames = GalaxyService.CACHE_NAME)
@RequiredArgsConstructor
public class GalaxyService extends SpaceEntityService<Galaxy> {

    public static final String CACHE_NAME = "galaxy";

    private final GalaxyRepository repository;
    private final StarRepository starRepository;
    private final EventPublisher eventPublisher;

    public Page<Galaxy> getList(Specification<Galaxy> specification, Pageable pageable) {
        return repository.findAll(specification, pageable);
    }

    @Cacheable
    public Galaxy get(UUID id) throws AppException {
        return repository.findById(id)
            .orElseThrow(() -> new AppException(ErrorCodeType.NOT_FOUND_ENTITY));
    }

    @Transactional
    public Galaxy add(Galaxy entity) throws AppException {
        validate(entity, false, repository);
        Galaxy result = repository.save(entity);

        eventPublisher.publish(EventType.GALAXY_ADD, result.getId());

        return result;
    }

    @Transactional
    @CachePut(key = "#entity.id", condition = "caches[0].get(#entity.id) != null")
    public Galaxy update(Galaxy entity) throws AppException {
        validate(entity, true, repository);
        Galaxy result = repository.save(entity);

        eventPublisher.publish(EventType.GALAXY_UPDATE, entity.getId());

        return result;
    }

    @Transactional
    @CacheEvict
    public void delete(UUID id) throws AppException {
        if (!repository.existsById(id)) {
            throw new AppException(ErrorCodeType.NOT_FOUND_ENTITY);
        }

        if (starRepository.existsByGalaxyId(id)) {
            throw new AppException(ErrorCodeType.IN_USE);
        }

        repository.deleteById(id);
        eventPublisher.publish(EventType.GALAXY_DELETE, id);
    }

    @Override
    void validateEntity(Galaxy entity, boolean isUpdate) {
        // there is no entity-specific validation yet
    }
}
