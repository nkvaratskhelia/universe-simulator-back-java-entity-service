package com.example.universe.simulator.entityservice.services;

import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.repositories.GalaxyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GalaxyService {

    private final GalaxyRepository repository;

    public List<Galaxy> getList() {
        return repository.findAll();
    }

    public Galaxy get(UUID id) throws AppException {
        return repository.findById(id).orElseThrow(() -> new AppException(ErrorCodeType.ENTITY_NOT_FOUND));
    }

    @Transactional
    public Galaxy add(Galaxy entity) {
        return repository.save(entity);
    }

    @Transactional
    public Galaxy update(Galaxy entity) throws AppException {
        //validate entity with id exists
        get(entity.getId());

        return repository.save(entity);
    }

    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
