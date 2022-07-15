package com.example.universe.simulator.entityservice.controllers.graphql;

import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.filters.PlanetFilter;
import com.example.universe.simulator.entityservice.inputs.AddPlanetInput;
import com.example.universe.simulator.entityservice.inputs.UpdatePlanetInput;
import com.example.universe.simulator.entityservice.mappers.PlanetMapper;
import com.example.universe.simulator.entityservice.services.PlanetService;
import com.example.universe.simulator.entityservice.specifications.PlanetSpecificationBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PlanetGraphQLController extends AbstractGraphQLController {

    private final PlanetService service;
    private final PlanetSpecificationBuilder specificationBuilder;
    private final PlanetMapper mapper;

    @QueryMapping
    public Page<PlanetDto> getPlanets(@Argument String name, @Argument AbstractGraphQLController.PageInput pageInput) {
        var filter = PlanetFilter.builder()
            .name(name)
            .build();
        PageRequest pageRequest = assemblePageRequest(pageInput);
        log.info("calling getList with filter {} and page {}", filter, pageRequest);
        Specification<Planet> specification = specificationBuilder.build(filter);

        Page<PlanetDto> result = service.getList(specification, pageRequest)
            .map(mapper::toDto);
        log.info("fetched [{}] record(s)", result.getNumberOfElements());

        return result;
    }

    @QueryMapping
    public PlanetDto getPlanet(@Argument UUID id) throws AppException {
        log.info("calling get with id [{}]", id);
        PlanetDto result = mapper.toDto(service.get(id));
        log.info("fetched [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public PlanetDto addPlanet(@Argument @Valid AddPlanetInput input) throws AppException {
        log.info("calling add with {}", input);

        Planet entity = mapper.toEntity(input);
        PlanetDto result = mapper.toDto(service.add(entity));
        log.info("added [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public PlanetDto updatePlanet(@Argument @Valid UpdatePlanetInput input) throws AppException {
        log.info("calling update with {}", input);

        Planet entity = mapper.toEntity(input);
        PlanetDto result = mapper.toDto(service.update(entity));
        log.info("updated [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public UUID deletePlanet(@Argument UUID id) throws AppException {
        log.info("calling delete with id [{}]", id);
        service.delete(id);
        log.info("deleted [{}]", id);

        return id;
    }
}
