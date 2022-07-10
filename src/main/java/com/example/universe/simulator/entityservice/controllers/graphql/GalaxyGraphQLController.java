package com.example.universe.simulator.entityservice.controllers.graphql;

import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.filters.GalaxyFilter;
import com.example.universe.simulator.entityservice.services.GalaxyService;
import com.example.universe.simulator.entityservice.specifications.GalaxySpecificationBuilder;
import com.example.universe.simulator.entityservice.validators.GalaxyDtoValidator;
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
public class GalaxyGraphQLController {

    private final GalaxyService service;
    private final GalaxyDtoValidator validator;
    private final GalaxySpecificationBuilder specificationBuilder;
    private final ModelMapper modelMapper;

    @QueryMapping
    public GalaxyDto getGalaxy(@Argument UUID id) throws AppException {
        log.info("calling get with id [{}]", id);
        GalaxyDto result = modelMapper.map(service.get(id), GalaxyDto.class);
        log.info("fetched [{}]", result.getId());

        return result;
    }

    @QueryMapping
    public Page<GalaxyDto> getGalaxies(@Argument String name, @Argument int page, @Argument int size) {
        var filter = GalaxyFilter.builder()
                .name(name)
                .build();
        log.info("calling getList with filter [{}] and page {} and size {}", filter, page, size);
        Specification<Galaxy> specification = specificationBuilder.build(filter);
        Page<GalaxyDto> result = service.getList(specification, PageRequest.of(page, size))
                .map(item -> modelMapper.map(item, GalaxyDto.class));
        log.info("fetched [{}] record(s)", result.getNumberOfElements());

        return result;

    }

    @MutationMapping
    public GalaxyDto addGalaxy(@Argument AddGalaxyInput input) throws AppException {
        log.info("calling add with {}", input);
        GalaxyDto dto = GalaxyDto.builder()
                .name(input.name())
                .build();
        validator.validate(dto, false);

        Galaxy entity = modelMapper.map(dto, Galaxy.class);
        GalaxyDto result = modelMapper.map(service.add(entity), GalaxyDto.class);
        log.info("added [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public GalaxyDto updateGalaxy(@Argument UpdateGalaxyInput input) throws AppException {
        log.info("calling update with {}", input);
        GalaxyDto dto = GalaxyDto.builder()
                .id(input.id())
                .name(input.name())
                .version(input.version())
                .build();
        validator.validate(dto, true);

        Galaxy entity = modelMapper.map(dto, Galaxy.class);
        GalaxyDto result = modelMapper.map(service.update(entity), GalaxyDto.class);
        log.info("updated [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public UUID deleteGalaxy(@Argument UUID id) throws AppException {
        log.info("calling delete with id [{}]", id);
        service.delete(id);
        log.info("deleted [{}]", id);

        return id;
    }

    record AddGalaxyInput(String name) {
    }

    record UpdateGalaxyInput(UUID id, String name, Long version) {
    }

}
