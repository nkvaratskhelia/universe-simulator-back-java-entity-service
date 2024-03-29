package com.example.universe.simulator.entityservice.controllers.graphql;

import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.filters.StarFilter;
import com.example.universe.simulator.entityservice.inputs.AddStarInput;
import com.example.universe.simulator.entityservice.inputs.PageInput;
import com.example.universe.simulator.entityservice.inputs.UpdateStarInput;
import com.example.universe.simulator.entityservice.mappers.PageInputMapper;
import com.example.universe.simulator.entityservice.mappers.StarMapper;
import com.example.universe.simulator.entityservice.services.StarService;
import com.example.universe.simulator.entityservice.specifications.StarSpecificationBuilder;
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
public class StarGraphQLController {

    private final StarService service;
    private final StarSpecificationBuilder specificationBuilder;
    private final StarMapper starMapper;
    private final PageInputMapper pageInputMapper;

    @QueryMapping
    public Page<StarDto> getStars(@Argument String name, @Argument @Valid PageInput pageInput) {
        var filter = StarFilter.builder()
            .name(name)
            .build();
        Pageable pageable = pageInputMapper.toPageable(pageInput);
        log.info("calling getStars with {} and {}", filter, pageable);
        Specification<Star> specification = specificationBuilder.build(filter);

        Page<StarDto> result = service.getList(specification, pageable)
            .map(starMapper::toDto);
        log.info("fetched [{}] record(s)", result.getNumberOfElements());

        return result;
    }

    @QueryMapping
    public StarDto getStar(@Argument UUID id) throws AppException {
        log.info("calling getStar with id [{}]", id);
        StarDto result = starMapper.toDto(service.get(id));
        log.info("fetched [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public StarDto addStar(@Argument @Valid AddStarInput input) throws AppException {
        log.info("calling addStar with {}", input);

        Star entity = service.add(starMapper.toEntity(input));
        StarDto result = starMapper.toDto(entity);
        log.info("added [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public StarDto updateStar(@Argument @Valid UpdateStarInput input) throws AppException {
        log.info("calling updateStar with {}", input);

        Star entity = service.update(starMapper.toEntity(input));
        StarDto result = starMapper.toDto(entity);
        log.info("updated [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public UUID deleteStar(@Argument UUID id) throws AppException {
        log.info("calling deleteStar with id [{}]", id);
        service.delete(id);
        log.info("deleted [{}]", id);

        return id;
    }
}
