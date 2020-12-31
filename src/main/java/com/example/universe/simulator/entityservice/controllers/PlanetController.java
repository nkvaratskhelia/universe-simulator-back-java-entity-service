package com.example.universe.simulator.entityservice.controllers;

import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.services.PlanetService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("planet")
@RequiredArgsConstructor
public class PlanetController {

    private final ModelMapper modelMapper;

    private final PlanetService service;

    @GetMapping("get-list")
    private List<PlanetDto> getList() {
        return modelMapper.map(service.getList(), new TypeToken<List<PlanetDto>>() {}.getType());
    }

    @GetMapping("get/{id}")
    private PlanetDto get(@PathVariable UUID id) throws AppException {
        return modelMapper.map(service.get(id), PlanetDto.class);
    }

    @PostMapping("add")
    private PlanetDto add(@RequestBody PlanetDto dto) throws AppException {
        dto.validate(false);

        Planet entity = modelMapper.map(dto, Planet.class);
        return modelMapper.map(service.add(entity), PlanetDto.class);
    }

    @PutMapping("update")
    private PlanetDto update(@RequestBody PlanetDto dto) throws AppException {
        dto.validate(true);

        Planet entity = modelMapper.map(dto, Planet.class);
        return modelMapper.map(service.update(entity), PlanetDto.class);
    }

    @DeleteMapping("delete/{id}")
    private void delete(@PathVariable UUID id) throws AppException {
        service.delete(id);
    }
}
