package com.example.universe.simulator.entityservice.services;

import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.repositories.PlanetRepository;
import com.example.universe.simulator.entityservice.repositories.StarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlanetService {

    private final PlanetRepository repository;
    private final StarRepository starRepository;

    public List<Planet> getList() {
        return repository.findAll();
    }

    public Planet get(UUID id) throws AppException {
        return repository.findById(id).orElseThrow(() -> new AppException(ErrorCodeType.NOT_FOUND_ENTITY));
    }

    @Transactional
    public Planet add(Planet entity) throws AppException {
        validate(entity, false);
        return repository.save(entity);
    }

    @Transactional
    public Planet update(Planet entity) throws AppException {
        validate(entity, true);
        return repository.save(entity);
    }

    @Transactional
    public void delete(UUID id) {
        // TODO check planet id in moons repo
        repository.deleteById(id);
    }

    private void validate(Planet entity, boolean isUpdate) throws AppException {
        if (isUpdate && !repository.existsById(entity.getId())) {
            throw new AppException(ErrorCodeType.NOT_FOUND_ENTITY);
        }

        boolean existsByName = isUpdate
                ? repository.existsByNameAndIdNot(entity.getName(), entity.getId())
                : repository.existsByName(entity.getName());
        if (existsByName) {
            throw new AppException(ErrorCodeType.EXISTS_NAME);
        }

        if (!starRepository.existsById(entity.getStar().getId())) {
            throw new AppException(ErrorCodeType.NOT_FOUND_STAR);
        }
    }

}
