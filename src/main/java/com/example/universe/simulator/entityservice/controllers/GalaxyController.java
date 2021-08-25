package com.example.universe.simulator.entityservice.controllers;

import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.filters.GalaxyFilter;
import com.example.universe.simulator.entityservice.services.GalaxyService;
import com.example.universe.simulator.entityservice.specifications.GalaxySpecificationBuilder;
import com.example.universe.simulator.entityservice.validators.GalaxyDtoValidator;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("galaxy")
@RequiredArgsConstructor
@Slf4j
public class GalaxyController {

    private final GalaxyService service;
    private final GalaxyDtoValidator validator;
    private final GalaxySpecificationBuilder specificationBuilder;
    private final ModelMapper modelMapper;

    @PostMapping("get-list")
    public Callable<Page<GalaxyDto>> getList(@RequestBody Optional<GalaxyFilter> filter, @ParameterObject Pageable pageable) {
        log.info("calling getList with filter [{}] and {}", filter.orElse(null), pageable);
        Specification<Galaxy> specification = specificationBuilder.build(filter.orElse(null));

        return () -> {
            Page<GalaxyDto> result = service.getList(specification, pageable)
                .map(item -> modelMapper.map(item, GalaxyDto.class));
            log.info("fetched [{}] record(s)", result.getNumberOfElements());

            return result;
        };
    }

    @GetMapping("get/{id}")
    public GalaxyDto get(@PathVariable UUID id) throws AppException {
        log.info("calling get with id [{}]", id);
        GalaxyDto result = modelMapper.map(service.get(id), GalaxyDto.class);
        log.info("fetched [{}]", result.getId());

        return result;
    }

    @PostMapping("add")
    public GalaxyDto add(@RequestBody GalaxyDto dto) throws AppException {
        log.info("calling add with {}", dto);
        validator.validate(dto, false);

        Galaxy entity = modelMapper.map(dto, Galaxy.class);
        GalaxyDto result = modelMapper.map(service.add(entity), GalaxyDto.class);
        log.info("added [{}]", result.getId());

        return result;
    }

    @PutMapping("update")
    public GalaxyDto update(@RequestBody GalaxyDto dto) throws AppException {
        log.info("calling update with {}", dto);
        validator.validate(dto, true);

        Galaxy entity = modelMapper.map(dto, Galaxy.class);
        GalaxyDto result = modelMapper.map(service.update(entity), GalaxyDto.class);
        log.info("updated [{}]", result.getId());

        return result;
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable UUID id) throws AppException {
        log.info("calling delete with id [{}]", id);
        service.delete(id);
        log.info("deleted [{}]", id);
    }
}
