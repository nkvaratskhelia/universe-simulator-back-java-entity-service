package com.example.universe.simulator.entityservice.controllers.graphql;

import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.filters.GalaxyFilter;
import com.example.universe.simulator.entityservice.mappers.GalaxyMapper;
import com.example.universe.simulator.entityservice.services.GalaxyService;
import com.example.universe.simulator.entityservice.specifications.GalaxySpecificationBuilder;
import com.example.universe.simulator.entityservice.validators.GalaxyDtoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class GalaxyGraphQLController extends AbstractGraphQLController {

    private final GalaxyService service;
    private final GalaxyDtoValidator validator;
    private final GalaxySpecificationBuilder specificationBuilder;
    private final GalaxyMapper mapper;

    @QueryMapping
    public Page<GalaxyDto> getGalaxies(@Argument String name, @Argument AbstractGraphQLController.PageInput pageInput) {
        var filter = GalaxyFilter.builder()
            .name(name)
            .build();
        PageRequest pageRequest = assemblePageRequest(pageInput);
        log.info("calling getList with filter [{}] and page {}", filter, pageRequest);
        Specification<Galaxy> specification = specificationBuilder.build(filter);

        Page<GalaxyDto> result = service.getList(specification, pageRequest)
            .map(mapper::toDto);
        log.info("fetched [{}] record(s)", result.getNumberOfElements());

        return result;

    }

    @QueryMapping
    public GalaxyDto getGalaxy(@Argument UUID id) throws AppException {
        log.info("calling get with id [{}]", id);
        GalaxyDto result = mapper.toDto(service.get(id));
        log.info("fetched [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public GalaxyDto addGalaxy(@Argument AddGalaxyInput input) throws AppException {
        log.info("calling add with {}", input);
        GalaxyDto dto = mapper.toDto(input);
        validator.validate(dto, false);

        Galaxy entity = mapper.toEntity(dto);
        GalaxyDto result = mapper.toDto(service.add(entity));
        log.info("added [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public GalaxyDto updateGalaxy(@Argument UpdateGalaxyInput input) throws AppException {
        log.info("calling update with {}", input);
        GalaxyDto dto = mapper.toDto(input);
        validator.validate(dto, true);

        Galaxy entity = mapper.toEntity(dto);
        GalaxyDto result = mapper.toDto(service.update(entity));
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

    public record AddGalaxyInput(String name) {
    }

    public record UpdateGalaxyInput(UUID id, String name, Long version) {
    }

}
