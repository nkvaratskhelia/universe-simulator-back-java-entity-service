package com.example.universe.simulator.entityservice.services;

import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.repositories.GalaxyRepository;
import com.example.universe.simulator.entityservice.repositories.StarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GalaxyService {

    private final GalaxyRepository repository;
    private final StarRepository starRepository;

    public Page<Galaxy> getList(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Galaxy get(UUID id) throws AppException {
        return repository.findById(id).orElseThrow(() -> new AppException(ErrorCodeType.NOT_FOUND_ENTITY));
    }

    @Transactional
    public Galaxy add(Galaxy entity) throws AppException {
        validate(entity, false);
        return repository.save(entity);
    }

    private void validate(Galaxy entity, boolean isUpdate) throws AppException {
        if (isUpdate && !repository.existsById(entity.getId())) {
            throw new AppException(ErrorCodeType.NOT_FOUND_ENTITY);
        }

        boolean existsByName = isUpdate
                ? repository.existsByNameAndIdNot(entity.getName(), entity.getId())
                : repository.existsByName(entity.getName());
        if (existsByName) {
            throw new AppException(ErrorCodeType.EXISTS_NAME);
        }
    }

    @Transactional
    public Galaxy update(Galaxy entity) throws AppException {
        validate(entity, true);
        return repository.save(entity);
    }

    @Transactional
    public void delete(UUID id) throws AppException {
        if (starRepository.existsByGalaxyId(id)) {
            throw new AppException(ErrorCodeType.IN_USE);
        }

        repository.deleteById(id);
    }
}
