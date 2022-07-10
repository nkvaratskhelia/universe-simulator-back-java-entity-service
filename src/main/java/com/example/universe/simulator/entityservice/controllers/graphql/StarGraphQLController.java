package com.example.universe.simulator.entityservice.controllers.graphql;

import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.filters.StarFilter;
import com.example.universe.simulator.entityservice.services.StarService;
import com.example.universe.simulator.entityservice.specifications.StarSpecificationBuilder;
import com.example.universe.simulator.entityservice.validators.StarDtoValidator;
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
public class StarGraphQLController {

    private final StarService service;
    private final StarDtoValidator validator;
    private final StarSpecificationBuilder specificationBuilder;
    private final ModelMapper modelMapper;

    @QueryMapping
    public StarDto getStar(@Argument UUID id) throws AppException {
        log.info("calling get with id [{}]", id);
        StarDto result = modelMapper.map(service.get(id), StarDto.class);
        log.info("fetched [{}]", result.getId());

        return result;
    }

    @QueryMapping
    public Page<StarDto> getStars(@Argument String name, @Argument int page, @Argument int size) {
        var filter = StarFilter.builder()
                .name(name)
                .build();
        log.info("calling getList with filter [{}] and page {} and size {}", filter, page, size);
        Specification<Star> specification = specificationBuilder.build(filter);
        Page<StarDto> result = service.getList(specification, PageRequest.of(page, size))
                .map(item -> modelMapper.map(item, StarDto.class));
        log.info("fetched [{}] record(s)", result.getNumberOfElements());

        return result;

    }

    @MutationMapping
    public StarDto addStar(@Argument AddStarInput input) throws AppException {
        log.info("calling add with {}, galaxy id [{}]", input, input.galaxyId());
        StarDto dto = StarDto.builder()
                .name(input.name())
                .galaxy(GalaxyDto.builder()
                        .id(input.galaxyId())
                        .build())
                .build();
        validator.validate(dto, false);

        Star entity = modelMapper.map(dto, Star.class);
        StarDto result = modelMapper.map(service.add(entity), StarDto.class);
        log.info("added [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public StarDto updateStar(@Argument UpdateStarInput input) throws AppException {
        log.info("calling update with {}, galaxy id [{}]", input, input.galaxyId());
        StarDto dto = StarDto.builder()
                .id(input.id())
                .name(input.name())
                .version(input.version())
                .galaxy(GalaxyDto.builder()
                        .id(input.galaxyId())
                        .build())
                .build();
        validator.validate(dto, true);

        Star entity = modelMapper.map(dto, Star.class);
        StarDto result = modelMapper.map(service.update(entity), StarDto.class);
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

    record AddStarInput(String name, UUID galaxyId) {
    }

    record UpdateStarInput(UUID id, String name, Long version, UUID galaxyId) {
    }

}
