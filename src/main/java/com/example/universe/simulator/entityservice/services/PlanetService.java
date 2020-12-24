package com.example.universe.simulator.entityservice.services;

import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.repositories.PlanetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlanetService {

    private final PlanetRepository repo;

    public Planet get(UUID id) throws AppException {
        return repo.findById(id).orElseThrow(() -> new AppException(ErrorCodeType.ENTITY_NOT_FOUND));
    }

    public List<Planet> getList() {
        return repo.findAll();
    }

    @Transactional
    public Planet add(Planet planet) {
        return repo.save(planet);
    }

    @Transactional
    public Planet update(Planet planet) throws AppException {
        get(planet.getId());
        return repo.save(planet);
    }

    @Transactional
    public void delete(UUID id) throws AppException {
        get(id);
        repo.deleteById(id);
    }

}
