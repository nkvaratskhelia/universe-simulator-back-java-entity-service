package com.example.universe.simulator.entityservice.controllers;

import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.filters.PlanetFilter;
import com.example.universe.simulator.entityservice.services.PlanetService;
import com.example.universe.simulator.entityservice.specifications.PlanetSpecification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("planet")
@RequiredArgsConstructor
public class PlanetController {

    private final ModelMapper modelMapper;

    private final PlanetService service;

    @PostMapping("get-list")
    private Callable<Page<PlanetDto>> getList(@RequestBody Optional<PlanetFilter> filter, Pageable pageable) {
        Specification<Planet> specification = filter.map(item -> new PlanetSpecification().getSpecification(item))
                .orElse(null);
        return () -> service.getList(specification, pageable)
                .map(item -> modelMapper.map(item, PlanetDto.class));
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
