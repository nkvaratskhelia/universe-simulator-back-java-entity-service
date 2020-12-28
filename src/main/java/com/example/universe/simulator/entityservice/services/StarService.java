package com.example.universe.simulator.entityservice.services;

import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.repositories.GalaxyRepository;
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

    public List<Star> getList() {
        return repository.findAll();
    }

    public Star get(UUID id) throws AppException {
        return repository.findById(id).orElseThrow(() -> new AppException(ErrorCodeType.ENTITY_NOT_FOUND));
    }

    @Transactional
    public Star add(Star entity) throws AppException {
        validate(entity, false);
        return repository.save(entity);
    }

    private void validate(Star entity, boolean isUpdate) throws AppException {
        if (isUpdate && !repository.existsById(entity.getId())) {
            throw new AppException(ErrorCodeType.ENTITY_NOT_FOUND);
        }

        boolean existsByName = isUpdate
                ? repository.existsByNameAndIdNot(entity.getName(), entity.getId())
                : repository.existsByName(entity.getName());
        if (existsByName) {
            throw new AppException(ErrorCodeType.EXISTS_NAME);
        }

        if (!galaxyRepository.existsById(entity.getGalaxy().getId())) {
            throw new AppException(ErrorCodeType.GALAXY_NOT_FOUND);
        }
    }

    @Transactional
    public Star update(Star entity) throws AppException {
        validate(entity, true);
        return repository.save(entity);
    }

    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
