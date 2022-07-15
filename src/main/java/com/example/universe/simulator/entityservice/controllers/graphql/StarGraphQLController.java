package com.example.universe.simulator.entityservice.controllers.graphql;

import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.filters.StarFilter;
import com.example.universe.simulator.entityservice.mappers.StarMapper;
import com.example.universe.simulator.entityservice.services.StarService;
import com.example.universe.simulator.entityservice.specifications.StarSpecificationBuilder;
import com.example.universe.simulator.entityservice.validators.StarDtoValidator;
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
public class StarGraphQLController extends AbstractGraphQLController {

    private final StarService service;
    private final StarDtoValidator validator;
    private final StarSpecificationBuilder specificationBuilder;
    private final StarMapper mapper;

    @QueryMapping
    public Page<StarDto> getStars(@Argument String name, @Argument AbstractGraphQLController.PageInput pageInput) {
        var filter = StarFilter.builder()
            .name(name)
            .build();
        PageRequest pageRequest = assemblePageRequest(pageInput);
        log.info("calling getList with filter {} and page {}", filter, pageRequest);
        Specification<Star> specification = specificationBuilder.build(filter);

        Page<StarDto> result = service.getList(specification, pageRequest)
            .map(mapper::toDto);
        log.info("fetched [{}] record(s)", result.getNumberOfElements());

        return result;
    }

    @QueryMapping
    public StarDto getStar(@Argument UUID id) throws AppException {
        log.info("calling get with id [{}]", id);
        StarDto result = mapper.toDto(service.get(id));
        log.info("fetched [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public StarDto addStar(@Argument AddStarInput input) throws AppException {
        log.info("calling add with {}", input);
        StarDto dto = mapper.toDto(input);
        validator.validate(dto, false);

        Star entity = mapper.toEntity(dto);
        StarDto result = mapper.toDto(service.add(entity));
        log.info("added [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public StarDto updateStar(@Argument UpdateStarInput input) throws AppException {
        log.info("calling update with {}", input);
        StarDto dto = mapper.toDto(input);
        validator.validate(dto, true);

        Star entity = mapper.toEntity(dto);
        StarDto result = mapper.toDto(service.update(entity));
        log.info("updated [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public UUID deleteStar(@Argument UUID id) throws AppException {
        log.info("calling delete with id [{}]", id);
        service.delete(id);
        log.info("deleted [{}]", id);

        return id;
    }

    public record AddStarInput(String name, UUID galaxyId) {}

    public record UpdateStarInput(UUID id, String name, Long version, UUID galaxyId) {}
}
