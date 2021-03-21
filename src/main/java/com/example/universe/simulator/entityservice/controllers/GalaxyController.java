package com.example.universe.simulator.entityservice.controllers;

import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.filters.GalaxyFilter;
import com.example.universe.simulator.entityservice.services.GalaxyService;
import com.example.universe.simulator.entityservice.specifications.GalaxySpecification;
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

    private final ModelMapper modelMapper;

    private final GalaxyService service;

    @PostMapping("get-list")
    private Callable<Page<GalaxyDto>> getList(@RequestBody Optional<GalaxyFilter> filter, @ParameterObject Pageable pageable) {
        log.info("calling getList with filter [{}] and {}", filter.orElse(null), pageable);
        Specification<Galaxy> specification = filter.map(item -> new GalaxySpecification().getSpecification(item))
            .orElse(null);
        return () -> service.getList(specification, pageable)
            .map(item -> modelMapper.map(item, GalaxyDto.class));
    }

    @GetMapping("get/{id}")
    private GalaxyDto get(@PathVariable UUID id) throws AppException {
        log.info("calling get with id [{}}]", id);
        return modelMapper.map(service.get(id), GalaxyDto.class);
    }

    @PostMapping("add")
    private GalaxyDto add(@RequestBody GalaxyDto dto) throws AppException {
        log.info("calling add with {}", dto);
        dto.validate(false);

        Galaxy entity = modelMapper.map(dto, Galaxy.class);
        return modelMapper.map(service.add(entity), GalaxyDto.class);
    }

    @PutMapping("update")
    private GalaxyDto update(@RequestBody GalaxyDto dto) throws AppException {
        log.info("calling update with {}", dto);
        dto.validate(true);

        Galaxy entity = modelMapper.map(dto, Galaxy.class);
        return modelMapper.map(service.update(entity), GalaxyDto.class);
    }

    @DeleteMapping("delete/{id}")
    private void delete(@PathVariable UUID id) throws AppException {
        log.info("calling delete with id [{}}]", id);
        service.delete(id);
    }
}
