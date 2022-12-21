package com.example.universe.simulator.entityservice.controllers.graphql;

import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.filters.PlanetFilter;
import com.example.universe.simulator.entityservice.inputs.AddPlanetInput;
import com.example.universe.simulator.entityservice.inputs.PageInput;
import com.example.universe.simulator.entityservice.inputs.UpdatePlanetInput;
import com.example.universe.simulator.entityservice.mappers.PageInputMapper;
import com.example.universe.simulator.entityservice.mappers.PlanetMapper;
import com.example.universe.simulator.entityservice.services.PlanetService;
import com.example.universe.simulator.entityservice.specifications.PlanetSpecificationBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PlanetGraphQLController {

    private final PlanetService service;
    private final PlanetSpecificationBuilder specificationBuilder;
    private final PlanetMapper planetMapper;
    private final PageInputMapper pageInputMapper;

    @QueryMapping
    public Page<PlanetDto> getPlanets(@Argument String name, @Argument @Valid PageInput pageInput) {
        var filter = PlanetFilter.builder()
            .name(name)
            .build();
        Pageable pageable = pageInputMapper.toPageable(pageInput);
        log.info("calling getPlanets with {} and {}", filter, pageable);
        Specification<Planet> specification = specificationBuilder.build(filter);

        Page<PlanetDto> result = service.getList(specification, pageable)
            .map(planetMapper::toDto);
        log.info("fetched [{}] record(s)", result.getNumberOfElements());

        return result;
    }

    @QueryMapping
    public PlanetDto getPlanet(@Argument UUID id) throws AppException {
        log.info("calling getPlanet with id [{}]", id);
        PlanetDto result = planetMapper.toDto(service.get(id));
        log.info("fetched [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public PlanetDto addPlanet(@Argument @Valid AddPlanetInput input) throws AppException {
        log.info("calling addPlanet with {}", input);

        Planet entity = service.add(planetMapper.toEntity(input));
        PlanetDto result = planetMapper.toDto(entity);
        log.info("added [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public PlanetDto updatePlanet(@Argument @Valid UpdatePlanetInput input) throws AppException {
        log.info("calling updatePlanet with {}", input);

        Planet entity = service.update(planetMapper.toEntity(input));
        PlanetDto result = planetMapper.toDto(entity);
        log.info("updated [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public UUID deletePlanet(@Argument UUID id) throws AppException {
        log.info("calling deletePlanet with id [{}]", id);
        service.delete(id);
        log.info("deleted [{}]", id);

        return id;
    }
}
