package com.example.universe.simulator.entityservice.services;

import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.repositories.GalaxyRepository;
import com.example.universe.simulator.entityservice.repositories.PlanetRepository;
import com.example.universe.simulator.entityservice.repositories.StarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StarService {

    private final StarRepository repository;
    private final GalaxyRepository galaxyRepository;
    private final PlanetRepository planetRepository;

    public List<Star> getList() {
        return repository.findAll();
    }

    public Star get(UUID id) throws AppException {
        return repository.findById(id).orElseThrow(() -> new AppException(ErrorCodeType.NOT_FOUND_ENTITY));
    }

    @Transactional
    public Star add(Star entity) throws AppException {
        validate(entity, false);
        return repository.save(entity);
    }

    @Transactional
    public Star update(Star entity) throws AppException {
        validate(entity, true);
        return repository.save(entity);
    }

    @Transactional
    public void delete(UUID id) throws AppException {
        if (planetRepository.existsByStarId(id)) {
            throw new AppException(ErrorCodeType.IN_USE);
        }

        repository.deleteById(id);
    }

    private void validate(Star entity, boolean isUpdate) throws AppException {
        if (isUpdate && !repository.existsById(entity.getId())) {
            throw new AppException(ErrorCodeType.NOT_FOUND_ENTITY);
        }

        boolean existsByName = isUpdate
                ? repository.existsByNameAndIdNot(entity.getName(), entity.getId())
                : repository.existsByName(entity.getName());
        if (existsByName) {
            throw new AppException(ErrorCodeType.EXISTS_NAME);
        }

        if (!galaxyRepository.existsById(entity.getGalaxy().getId())) {
            throw new AppException(ErrorCodeType.NOT_FOUND_GALAXY);
        }
    }

}
