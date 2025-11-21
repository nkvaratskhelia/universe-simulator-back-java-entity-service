package com.example.universe.simulator.entityservice.controllers.rest;

import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.filters.GalaxyFilter;
import com.example.universe.simulator.entityservice.inputs.AddGalaxyInput;
import com.example.universe.simulator.entityservice.inputs.UpdateGalaxyInput;
import com.example.universe.simulator.entityservice.mappers.GalaxyMapper;
import com.example.universe.simulator.entityservice.services.GalaxyService;
import com.example.universe.simulator.entityservice.specifications.GalaxySpecificationBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
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
@RequestMapping("galaxies")
@RequiredArgsConstructor
@Slf4j
public class GalaxyRestController {

    private final GalaxyService service;
    private final GalaxySpecificationBuilder specificationBuilder;
    private final GalaxyMapper mapper;

    @GetMapping
    public Callable<Page<@NonNull GalaxyDto>> getGalaxies(@RequestParam(required = false) String name, @ParameterObject Pageable pageable) {
        var filter = GalaxyFilter.builder()
            .name(name)
            .build();
        log.info("calling getGalaxies with {} and {}", filter, pageable);
        Specification<@NonNull Galaxy> specification = specificationBuilder.build(filter);

        return () -> {
            Page<@NonNull GalaxyDto> result = service.getList(specification, pageable)
                .map(mapper::toDto);
            log.info("fetched [{}] record(s)", result.getNumberOfElements());

            return result;
        };
    }

    @GetMapping("{id}")
    public GalaxyDto getGalaxy(@PathVariable UUID id) throws AppException {
        log.info("calling getGalaxy with id [{}]", id);
        GalaxyDto result = mapper.toDto(service.get(id));
        log.info("fetched [{}]", result.getId());

        return result;
    }

    @PostMapping
    public GalaxyDto addGalaxy(@RequestBody @Valid AddGalaxyInput input) throws AppException {
        log.info("calling addGalaxy with {}", input);

        Galaxy entity = service.add(mapper.toEntity(input));
        GalaxyDto result = mapper.toDto(entity);
        log.info("added [{}]", result.getId());

        return result;
    }

    @PutMapping
    public GalaxyDto updateGalaxy(@RequestBody @Valid UpdateGalaxyInput input) throws AppException {
        log.info("calling updateGalaxy with {}", input);

        Galaxy entity = service.update(mapper.toEntity(input));
        GalaxyDto result = mapper.toDto(entity);
        log.info("updated [{}]", result.getId());

        return result;
    }

    @DeleteMapping("{id}")
    public void deleteGalaxy(@PathVariable UUID id) throws AppException {
        log.info("calling deleteGalaxy with id [{}]", id);
        service.delete(id);
        log.info("deleted [{}]", id);
    }
}
