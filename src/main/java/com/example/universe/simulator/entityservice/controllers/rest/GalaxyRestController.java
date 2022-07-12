package com.example.universe.simulator.entityservice.controllers.rest;

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
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("galaxies")
@RequiredArgsConstructor
@Slf4j
public class GalaxyRestController {

    private final GalaxyService service;
    private final GalaxyDtoValidator validator;
    private final GalaxySpecificationBuilder specificationBuilder;
    private final GalaxyMapper mapper;

    @GetMapping
    public Callable<Page<GalaxyDto>> getList(@RequestParam(required = false) String name, @ParameterObject Pageable pageable) {
        var filter = GalaxyFilter.builder()
            .name(name)
            .build();
        log.info("calling getList with filter {} and {}", filter, pageable);
        Specification<Galaxy> specification = specificationBuilder.build(filter);

        return () -> {
            Page<GalaxyDto> result = service.getList(specification, pageable)
                .map(mapper::toDto);
            log.info("fetched [{}] record(s)", result.getNumberOfElements());

            return result;
        };
    }

    @GetMapping("{id}")
    public GalaxyDto get(@PathVariable UUID id) throws AppException {
        log.info("calling get with id [{}]", id);
        GalaxyDto result = mapper.toDto(service.get(id));
        log.info("fetched [{}]", result.getId());

        return result;
    }

    @PostMapping
    public GalaxyDto add(@RequestBody GalaxyDto dto) throws AppException {
        log.info("calling add with {}", dto);
        validator.validate(dto, false);

        GalaxyDto result = mapper.toDto(
            service.add(mapper.toEntity(dto))
        );
        log.info("added [{}]", result.getId());

        return result;
    }

    @PutMapping
    public GalaxyDto update(@RequestBody GalaxyDto dto) throws AppException {
        log.info("calling update with {}", dto);
        validator.validate(dto, true);

        GalaxyDto result = mapper.toDto(
            service.update(mapper.toEntity(dto))
        );
        log.info("updated [{}]", result.getId());

        return result;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable UUID id) throws AppException {
        log.info("calling delete with id [{}]", id);
        service.delete(id);
        log.info("deleted [{}]", id);
    }
}
