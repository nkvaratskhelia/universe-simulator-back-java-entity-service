package com.example.universe.simulator.entityservice.controllers.graphql;

import com.example.universe.simulator.entityservice.dtos.MoonDto;
import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.entities.Moon;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.filters.MoonFilter;
import com.example.universe.simulator.entityservice.services.MoonService;
import com.example.universe.simulator.entityservice.specifications.MoonSpecificationBuilder;
import com.example.universe.simulator.entityservice.validators.MoonDtoValidator;
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
public class MoonGraphQLController {

    private final MoonService service;
    private final MoonDtoValidator validator;
    private final MoonSpecificationBuilder specificationBuilder;
    private final ModelMapper modelMapper;

    @QueryMapping
    public MoonDto getMoon(@Argument UUID id) throws AppException {
        log.info("calling get with id [{}]", id);
        MoonDto result = modelMapper.map(service.get(id), MoonDto.class);
        log.info("fetched [{}]", result.getId());

        return result;
    }

    @QueryMapping
    public Page<MoonDto> getMoons(@Argument String name, @Argument int page, @Argument int size) {
        var filter = MoonFilter.builder()
                .name(name)
                .build();
        log.info("calling getList with filter [{}] and page {} and size {}", filter, page, size);
        Specification<Moon> specification = specificationBuilder.build(filter);
        Page<MoonDto> result = service.getList(specification, PageRequest.of(page, size))
                .map(item -> modelMapper.map(item, MoonDto.class));
        log.info("fetched [{}] record(s)", result.getNumberOfElements());

        return result;

    }

    @MutationMapping
    public MoonDto addMoon(@Argument AddMoonInput input) throws AppException {
        log.info("calling add with {}, planet id [{}]", input, input.planetId());
        MoonDto dto = MoonDto.builder()
                .name(input.name())
                .planet(PlanetDto.builder()
                        .id(input.planetId())
                        .build())
                .build();
        validator.validate(dto, false);

        Moon entity = modelMapper.map(dto, Moon.class);
        MoonDto result = modelMapper.map(service.add(entity), MoonDto.class);
        log.info("added [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public MoonDto updateMoon(@Argument UpdateMoonInput input) throws AppException {
        log.info("calling update with {}, planet id [{}]", input, input.planetId());
        MoonDto dto = MoonDto.builder()
                .id(input.id())
                .name(input.name())
                .version(input.version())
                .planet(PlanetDto.builder()
                        .id(input.planetId())
                        .build())
                .build();
        validator.validate(dto, true);

        Moon entity = modelMapper.map(dto, Moon.class);
        MoonDto result = modelMapper.map(service.update(entity), MoonDto.class);
        log.info("updated [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public UUID deleteMoon(@Argument UUID id) throws AppException {
        log.info("calling delete with id [{}]", id);
        service.delete(id);
        log.info("deleted [{}]", id);

        return id;
    }

    record AddMoonInput(String name, UUID planetId) {
    }

    record UpdateMoonInput(UUID id, String name, Long version, UUID planetId) {
    }

}
