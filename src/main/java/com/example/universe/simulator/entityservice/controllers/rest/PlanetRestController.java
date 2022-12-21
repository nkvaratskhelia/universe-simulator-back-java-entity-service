package com.example.universe.simulator.entityservice.controllers.rest;

import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.filters.PlanetFilter;
import com.example.universe.simulator.entityservice.inputs.AddPlanetInput;
import com.example.universe.simulator.entityservice.inputs.UpdatePlanetInput;
import com.example.universe.simulator.entityservice.mappers.PlanetMapper;
import com.example.universe.simulator.entityservice.services.PlanetService;
import com.example.universe.simulator.entityservice.specifications.PlanetSpecificationBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("planets")
@RequiredArgsConstructor
@Slf4j
public class PlanetRestController {

    private final PlanetService service;
    private final PlanetSpecificationBuilder specificationBuilder;
    private final PlanetMapper mapper;

    @GetMapping
    public Callable<Page<PlanetDto>> getPlanets(@RequestParam(required = false) String name, @ParameterObject Pageable pageable) {
        var filter = PlanetFilter.builder()
            .name(name)
            .build();
        log.info("calling getPlanets with {} and {}", filter, pageable);
        Specification<Planet> specification = specificationBuilder.build(filter);

        return () -> {
            Page<PlanetDto> result = service.getList(specification, pageable)
                .map(mapper::toDto);
            log.info("fetched [{}] record(s)", result.getNumberOfElements());

            return result;
        };
    }

    @GetMapping("{id}")
    public PlanetDto getPlanet(@PathVariable UUID id) throws AppException {
        log.info("calling getPlanet with id [{}]", id);
        PlanetDto result = mapper.toDto(service.get(id));
        log.info("fetched [{}]", result.getId());

        return result;
    }

    @PostMapping
    public PlanetDto addPlanet(@RequestBody @Valid AddPlanetInput input) throws AppException {
        log.info("calling addPlanet with {}", input);

        Planet entity = service.add(mapper.toEntity(input));
        PlanetDto result = mapper.toDto(entity);
        log.info("added [{}]", result.getId());

        return result;
    }

    @PutMapping
    public PlanetDto updatePlanet(@RequestBody @Valid UpdatePlanetInput input) throws AppException {
        log.info("calling updatePlanet with {}", input);

        Planet entity = service.update(mapper.toEntity(input));
        PlanetDto result = mapper.toDto(entity);
        log.info("updated [{}]", result.getId());

        return result;
    }

    @DeleteMapping("{id}")
    public void deletePlanet(@PathVariable UUID id) throws AppException {
        log.info("calling deletePlanet with id [{}]", id);
        service.delete(id);
        log.info("deleted [{}]", id);
    }
}
