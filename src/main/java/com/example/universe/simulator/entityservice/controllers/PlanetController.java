package com.example.universe.simulator.entityservice.controllers;

import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.services.PlanetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/planet")
public class PlanetController {

    private final PlanetService service;

    @GetMapping("/get/{id}")
    public Planet get(@PathVariable UUID id) throws AppException {
        return service.get(id);
    }

    @GetMapping("/get-list")
    public List<Planet> getList() {
        return service.getList();
    }

    @PostMapping("/add")
    public Planet add(@RequestBody Planet planet) {
        return service.add(planet);
    }

    @PutMapping("/update")
    public Planet update(@RequestBody Planet planet) throws AppException {
        return service.update(planet);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable UUID id) throws AppException {
        service.delete(id);
    }

}
