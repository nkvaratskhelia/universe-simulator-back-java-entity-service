package com.example.universe.simulator.entityservice.controllers.graphql;

import com.example.universe.simulator.entityservice.dtos.MoonDto;
import com.example.universe.simulator.entityservice.entities.Moon;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.filters.MoonFilter;
import com.example.universe.simulator.entityservice.inputs.AddMoonInput;
import com.example.universe.simulator.entityservice.inputs.UpdateMoonInput;
import com.example.universe.simulator.entityservice.mappers.MoonMapper;
import com.example.universe.simulator.entityservice.services.MoonService;
import com.example.universe.simulator.entityservice.specifications.MoonSpecificationBuilder;
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
public class MoonGraphQLController extends AbstractGraphQLController {

    private final MoonService service;
    private final MoonSpecificationBuilder specificationBuilder;
    private final MoonMapper mapper;

    @QueryMapping
    public Page<MoonDto> getMoons(@Argument String name, @Argument AbstractGraphQLController.PageInput pageInput) {
        var filter = MoonFilter.builder()
            .name(name)
            .build();
        Pageable pageable = assemblePageRequest(pageInput);
        log.info("calling getMoons with {} and {}", filter, pageable);
        Specification<Moon> specification = specificationBuilder.build(filter);

        Page<MoonDto> result = service.getList(specification, pageable)
            .map(mapper::toDto);
        log.info("fetched [{}] record(s)", result.getNumberOfElements());

        return result;
    }

    @QueryMapping
    public MoonDto getMoon(@Argument UUID id) throws AppException {
        log.info("calling getMoon with id [{}]", id);
        MoonDto result = mapper.toDto(service.get(id));
        log.info("fetched [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public MoonDto addMoon(@Argument @Valid AddMoonInput input) throws AppException {
        log.info("calling addMoon with {}", input);

        Moon entity = service.add(mapper.toEntity(input));
        MoonDto result = mapper.toDto(entity);
        log.info("added [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public MoonDto updateMoon(@Argument @Valid UpdateMoonInput input) throws AppException {
        log.info("calling updateMoon with {}", input);

        Moon entity = service.update(mapper.toEntity(input));
        MoonDto result = mapper.toDto(entity);
        log.info("updated [{}]", result.getId());

        return result;
    }

    @MutationMapping
    public UUID deleteMoon(@Argument UUID id) {
        log.info("calling deleteMoon with id [{}]", id);
        service.delete(id);
        log.info("deleted [{}]", id);

        return id;
    }
}
