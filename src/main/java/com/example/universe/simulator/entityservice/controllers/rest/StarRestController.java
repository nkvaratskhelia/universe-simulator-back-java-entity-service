package com.example.universe.simulator.entityservice.controllers.rest;

import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.filters.StarFilter;
import com.example.universe.simulator.entityservice.inputs.AddStarInput;
import com.example.universe.simulator.entityservice.inputs.UpdateStarInput;
import com.example.universe.simulator.entityservice.mappers.StarMapper;
import com.example.universe.simulator.entityservice.services.StarService;
import com.example.universe.simulator.entityservice.specifications.StarSpecificationBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
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
@RequestMapping("stars")
@RequiredArgsConstructor
@Slf4j
public class StarRestController {

    private final StarService service;
    private final StarSpecificationBuilder specificationBuilder;
    private final StarMapper mapper;

    @GetMapping
    public Callable<Page<StarDto>> getStars(@RequestParam(required = false) String name, @ParameterObject Pageable pageable) {
        var filter = StarFilter.builder()
            .name(name)
            .build();
        log.info("calling getStars with {} and {}", filter, pageable);
        Specification<Star> specification = specificationBuilder.build(filter);

        return () -> {
            Page<StarDto> result = service.getList(specification, pageable)
                .map(mapper::toDto);
            log.info("fetched [{}] record(s)", result.getNumberOfElements());

            return result;
        };
    }

    @GetMapping("{id}")
    public StarDto getStar(@PathVariable UUID id) throws AppException {
        log.info("calling getStar with id [{}]", id);
        StarDto result = mapper.toDto(service.get(id));
        log.info("fetched [{}]", result.getId());

        return result;
    }

    @PostMapping
    public StarDto addStar(@RequestBody @Valid AddStarInput input) throws AppException {
        log.info("calling addStar with {}", input);

        Star entity = service.add(mapper.toEntity(input));
        StarDto result = mapper.toDto(entity);
        log.info("added [{}]", result.getId());

        return result;
    }

    @PutMapping
    public StarDto updateStar(@RequestBody @Valid UpdateStarInput input) throws AppException {
        log.info("calling updateStar with {}", input);

        Star entity = service.update(mapper.toEntity(input));
        StarDto result = mapper.toDto(entity);
        log.info("updated [{}]", result.getId());

        return result;
    }

    @DeleteMapping("{id}")
    public void deleteStar(@PathVariable UUID id) throws AppException {
        log.info("calling deleteStar with id [{}]", id);
        service.delete(id);
        log.info("deleted [{}]", id);
    }
}
