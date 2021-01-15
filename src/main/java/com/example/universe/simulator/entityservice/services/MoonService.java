package com.example.universe.simulator.entityservice.services;

import com.example.universe.simulator.entityservice.entities.Moon;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.repositories.MoonRepository;
import com.example.universe.simulator.entityservice.repositories.PlanetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MoonService {

    private final MoonRepository repository;
    private final PlanetRepository planetRepository;

    public Page<Moon> getList(Specification<Moon> specification, Pageable pageable) {
        return repository.findAll(specification, pageable);
    }

    public Moon get(UUID id) throws AppException {
        return repository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCodeType.NOT_FOUND_ENTITY));
    }

    @Transactional
    public Moon add(Moon entity) throws AppException {
        validate(entity, false);
        return repository.save(entity);
    }

    private void validate(Moon entity, boolean isUpdate) throws AppException {
        if (isUpdate && !repository.existsById(entity.getId())) {
            throw new AppException(ErrorCodeType.NOT_FOUND_ENTITY);
        }

        boolean existsByName = isUpdate
                ? repository.existsByNameAndIdNot(entity.getName(), entity.getId())
                : repository.existsByName(entity.getName());
        if (existsByName) {
            throw new AppException(ErrorCodeType.EXISTS_NAME);
        }

        if (!planetRepository.existsById(entity.getPlanet().getId())) {
            throw new AppException(ErrorCodeType.NOT_FOUND_PLANET);
        }
    }

    @Transactional
    public Moon update(Moon entity) throws AppException {
        validate(entity, true);
        return repository.save(entity);
    }

    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
