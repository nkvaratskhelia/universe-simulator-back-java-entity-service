package com.example.universe.simulator.entityservice.controllers.graphql;

import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.filters.PlanetFilter;
import com.example.universe.simulator.entityservice.services.PlanetService;
import com.example.universe.simulator.entityservice.specifications.PlanetSpecificationBuilder;
import com.example.universe.simulator.entityservice.validators.PlanetDtoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final PlanetDtoValidator validator;
    private final PlanetSpecificationBuilder specificationBuilder;
    private final ModelMapper modelMapper;

    @QueryMapping
    public PlanetDto getPlanet(@Argument UUID id) throws AppException {
        log.info("calling get with id [{}]", id);
        PlanetDto result = modelMapper.map(service.get(id), PlanetDto.class);
        log.info("fetched [{}]", result.getId());

        return result;
    }

    @QueryMapping
    public Page<PlanetDto> getPlanets(@Argument String name, @Argument int page, @Argument int size) {
        var filter = PlanetFilter.builder()
                .name(name)
                .build();
        log.info("calling getList with filter [{}] and page {} and size {}", filter, page, size);
        Specification<Planet> specification = specificationBuilder.build(filter);
        Page<PlanetDto> result = service.getList(specification, PageRequest.of(page, size))
                .map(item -> modelMapper.map(item, PlanetDto.class));
        log.info("fetched [{}] record(s)", result.getNumberOfElements());

        return result;

    }

    @MutationMapping
    public PlanetDto addPlanet(@Argument AddPlanetInput input) throws AppException {
        log.info("calling add with {}, star id [{}]", input, input.starId());
        PlanetDto dto = PlanetDto.builder()
                .name(input.name())
                .star(StarDto.builder()
                        .id(input.starId())
                        .build())
                .build();
        validator.validate(dto, false);

        Planet entity = modelMapper.map(dto, Planet.class);
        PlanetDto result = modelMapper.map(service.add(entity), PlanetDto.class);
        log.info("added [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public PlanetDto updatePlanet(@Argument UpdatePlanetInput input) throws AppException {
        log.info("calling update with {}, star id [{}]", input, input.starId());
        PlanetDto dto = PlanetDto.builder()
                .id(input.id())
                .name(input.name())
                .version(input.version())
                .star(StarDto.builder()
                        .id(input.starId())
                        .build())
                .build();
        validator.validate(dto, true);

        Planet entity = modelMapper.map(dto, Planet.class);
        PlanetDto result = modelMapper.map(service.update(entity), PlanetDto.class);
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

    record AddPlanetInput(String name, UUID starId) {
    }

    record UpdatePlanetInput(UUID id, String name, Long version, UUID starId) {
    }

}
