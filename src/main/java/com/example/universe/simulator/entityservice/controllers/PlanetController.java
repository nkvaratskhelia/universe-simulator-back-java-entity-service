package com.example.universe.simulator.entityservice.controllers;

import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.filters.PlanetFilter;
import com.example.universe.simulator.entityservice.services.PlanetService;
import com.example.universe.simulator.entityservice.specifications.PlanetSpecification;
import com.example.universe.simulator.entityservice.validators.PlanetDtoValidator;
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
@RequestMapping("planet")
@RequiredArgsConstructor
@Slf4j
public class PlanetController {

    private final PlanetService service;
    private final PlanetDtoValidator validator;
    private final ModelMapper modelMapper;

    @PostMapping("get-list")
    public Callable<Page<PlanetDto>> getList(@RequestBody Optional<PlanetFilter> filter, @ParameterObject Pageable pageable) {
        log.info("calling getList with filter [{}] and {}", filter.orElse(null), pageable);

        Specification<Planet> specification = filter
            .map(item -> new PlanetSpecification().getSpecification(item))
            .orElse(null);

        return () -> {
            Page<PlanetDto> result = service.getList(specification, pageable)
                .map(item -> modelMapper.map(item, PlanetDto.class));
            log.info("fetched [{}] record(s)", result.getNumberOfElements());

            return result;
        };
    }

    @GetMapping("get/{id}")
    public PlanetDto get(@PathVariable UUID id) throws AppException {
        log.info("calling get with id [{}]", id);
        PlanetDto result = modelMapper.map(service.get(id), PlanetDto.class);
        log.info("fetched [{}]", result.getId());

        return result;
    }

    @PostMapping("add")
    public PlanetDto add(@RequestBody PlanetDto dto) throws AppException {
        log.info("calling add with {}", dto);
        validator.validate(dto, false);

        Planet entity = modelMapper.map(dto, Planet.class);
        PlanetDto result = modelMapper.map(service.add(entity), PlanetDto.class);
        log.info("added [{}]", result.getId());

        return result;
    }

    @PutMapping("update")
    public PlanetDto update(@RequestBody PlanetDto dto) throws AppException {
        log.info("calling update with {}", dto);
        validator.validate(dto, true);

        Planet entity = modelMapper.map(dto, Planet.class);
        PlanetDto result = modelMapper.map(service.update(entity), PlanetDto.class);
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
