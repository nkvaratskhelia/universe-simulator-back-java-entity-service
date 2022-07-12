package com.example.universe.simulator.entityservice.controllers.graphql;

import com.example.universe.simulator.entityservice.dtos.MoonDto;
import com.example.universe.simulator.entityservice.entities.Moon;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.filters.MoonFilter;
import com.example.universe.simulator.entityservice.mappers.MoonMapper;
import com.example.universe.simulator.entityservice.services.MoonService;
import com.example.universe.simulator.entityservice.specifications.MoonSpecificationBuilder;
import com.example.universe.simulator.entityservice.validators.MoonDtoValidator;
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
public class MoonGraphQLController extends AbstractGraphQLController {

    private final MoonService service;
    private final MoonDtoValidator validator;
    private final MoonSpecificationBuilder specificationBuilder;
    private final MoonMapper mapper;

    @QueryMapping
    public Page<MoonDto> getMoons(@Argument String name, @Argument AbstractGraphQLController.PageInput pageInput) {
        var filter = MoonFilter.builder()
            .name(name)
            .build();
        PageRequest pageRequest = assemblePageRequest(pageInput);
        log.info("calling getList with filter [{}] and page {}", filter, pageRequest);
        Specification<Moon> specification = specificationBuilder.build(filter);

        Page<MoonDto> result = service.getList(specification, pageRequest)
            .map(mapper::toDto);
        log.info("fetched [{}] record(s)", result.getNumberOfElements());

        return result;

    }

    @QueryMapping
    public MoonDto getMoon(@Argument UUID id) throws AppException {
        log.info("calling get with id [{}]", id);
        MoonDto result = mapper.toDto(service.get(id));
        log.info("fetched [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public MoonDto addMoon(@Argument AddMoonInput input) throws AppException {
        log.info("calling add with {}", input);
        MoonDto dto = MoonDto.builder()
            .name(input.name())
            .planetId(input.planetId())
            .build();
        validator.validate(dto, false);

        Moon entity = mapper.toEntity(dto);
        MoonDto result = mapper.toDto(service.add(entity));
        log.info("added [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public MoonDto updateMoon(@Argument UpdateMoonInput input) throws AppException {
        log.info("calling update with {}", input);
        MoonDto dto = MoonDto.builder()
            .id(input.id())
            .name(input.name())
            .version(input.version())
            .planetId(input.planetId())
            .build();
        validator.validate(dto, true);

        Moon entity = mapper.toEntity(dto);
        MoonDto result = mapper.toDto(service.update(entity));
        log.info("updated [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public UUID deleteMoon(@Argument UUID id) {
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