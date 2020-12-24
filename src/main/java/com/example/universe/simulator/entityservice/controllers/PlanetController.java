package com.example.universe.simulator.entityservice.controllers;

import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.services.PlanetService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/planet")
public class PlanetController {

    private final ModelMapper modelMapper;

    private final PlanetService service;

    @GetMapping("/get/{id}")
    public PlanetDto get(@PathVariable UUID id) throws AppException {
        return modelMapper.map(service.get(id), PlanetDto.class);
    }

    @GetMapping("/get-list")
    public List<PlanetDto> getList() {
        return modelMapper.map(service.getList(), new TypeToken<List<PlanetDto>>() {}.getType());
    }

    @PostMapping("/add")
    public PlanetDto add(@RequestBody PlanetDto planetDto) throws AppException {
        planetDto.validate(false);
        Planet planet = modelMapper.map(planetDto, Planet.class);
        return modelMapper.map(service.add(planet), PlanetDto.class);
    }

    @PutMapping("/update")
    public PlanetDto update(@RequestBody PlanetDto planetDto) throws AppException {
        planetDto.validate(true);
        Planet planet = modelMapper.map(planetDto, Planet.class);
        return modelMapper.map(service.update(planet), PlanetDto.class);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable UUID id) throws AppException {
        service.delete(id);
    }

}
