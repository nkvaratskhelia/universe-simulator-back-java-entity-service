package com.example.universe.simulator.entityservice.controllers.graphql;

import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.filters.GalaxyFilter;
import com.example.universe.simulator.entityservice.inputs.AddGalaxyInput;
import com.example.universe.simulator.entityservice.inputs.UpdateGalaxyInput;
import com.example.universe.simulator.entityservice.mappers.GalaxyMapper;
import com.example.universe.simulator.entityservice.services.GalaxyService;
import com.example.universe.simulator.entityservice.specifications.GalaxySpecificationBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class GalaxyGraphQLController extends AbstractGraphQLController {

    private final GalaxyService service;
    private final GalaxySpecificationBuilder specificationBuilder;
    private final GalaxyMapper mapper;

    @QueryMapping
    public Page<GalaxyDto> getGalaxies(@Argument String name, @Argument AbstractGraphQLController.PageInput pageInput) {
        var filter = GalaxyFilter.builder()
            .name(name)
            .build();
        Pageable pageable = assemblePageRequest(pageInput);
        log.info("calling getGalaxies with {} and {}", filter, pageable);
        Specification<Galaxy> specification = specificationBuilder.build(filter);

        Page<GalaxyDto> result = service.getList(specification, pageable)
            .map(mapper::toDto);
        log.info("fetched [{}] record(s)", result.getNumberOfElements());

        return result;
    }

    @QueryMapping
    public GalaxyDto getGalaxy(@Argument UUID id) throws AppException {
        log.info("calling getGalaxy with id [{}]", id);
        GalaxyDto result = mapper.toDto(service.get(id));
        log.info("fetched [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public GalaxyDto addGalaxy(@Argument @Valid AddGalaxyInput input) throws AppException {
        log.info("calling addGalaxy with {}", input);

        Galaxy entity = service.add(mapper.toEntity(input));
        GalaxyDto result = mapper.toDto(entity);
        log.info("added [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public GalaxyDto updateGalaxy(@Argument @Valid UpdateGalaxyInput input) throws AppException {
        log.info("calling updateGalaxy with {}", input);

        Galaxy entity = service.update(mapper.toEntity(input));
        GalaxyDto result = mapper.toDto(entity);
        log.info("updated [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public UUID deleteGalaxy(@Argument UUID id) throws AppException {
        log.info("calling deleteGalaxy with id [{}]", id);
        service.delete(id);
        log.info("deleted [{}]", id);

        return id;
    }
}
