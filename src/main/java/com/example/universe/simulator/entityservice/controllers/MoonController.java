package com.example.universe.simulator.entityservice.controllers;

import com.example.universe.simulator.entityservice.dtos.MoonDto;
import com.example.universe.simulator.entityservice.entities.Moon;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.filters.MoonFilter;
import com.example.universe.simulator.entityservice.services.MoonService;
import com.example.universe.simulator.entityservice.specifications.MoonSpecificationBuilder;
import com.example.universe.simulator.entityservice.validators.MoonDtoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
@RequestMapping("moons")
@RequiredArgsConstructor
@Slf4j
public class MoonController {

    private final MoonService service;
    private final MoonDtoValidator validator;
    private final MoonSpecificationBuilder specificationBuilder;
    private final ModelMapper modelMapper;

    @GetMapping
    public Callable<Page<MoonDto>> getList(@RequestParam(required = false) String name, @ParameterObject Pageable pageable) {
        var filter = MoonFilter.builder()
            .name(name)
            .build();
        log.info("calling getList with filter [{}] and {}", filter, pageable);
        Specification<Moon> specification = specificationBuilder.build(filter);

        return () -> {
            Page<MoonDto> result = service.getList(specification, pageable)
                .map(item -> modelMapper.map(item, MoonDto.class));
            log.info("fetched [{}] record(s)", result.getNumberOfElements());

            return result;
        };
    }

    @GetMapping("{id}")
    public MoonDto get(@PathVariable UUID id) throws AppException {
        log.info("calling get with id [{}]", id);
        MoonDto result = modelMapper.map(service.get(id), MoonDto.class);
        log.info("fetched [{}]", result.getId());

        return result;
    }

    @PostMapping
    public MoonDto add(@RequestBody MoonDto dto) throws AppException {
        log.info("calling add with {}, planet id [{}]", dto, dto.getPlanet().getId());
        validator.validate(dto, false);

        Moon entity = modelMapper.map(dto, Moon.class);
        MoonDto result = modelMapper.map(service.add(entity), MoonDto.class);
        log.info("added [{}]", result.getId());

        return result;
    }

    @PutMapping
    public MoonDto update(@RequestBody MoonDto dto) throws AppException {
        log.info("calling update with {}, planet id [{}]", dto, dto.getPlanet().getId());
        validator.validate(dto, true);

        Moon entity = modelMapper.map(dto, Moon.class);
        MoonDto result = modelMapper.map(service.update(entity), MoonDto.class);
        log.info("updated [{}]", result.getId());

        return result;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable UUID id) {
        log.info("calling delete with id [{}]", id);
        service.delete(id);
        log.info("deleted [{}]", id);
    }
}
