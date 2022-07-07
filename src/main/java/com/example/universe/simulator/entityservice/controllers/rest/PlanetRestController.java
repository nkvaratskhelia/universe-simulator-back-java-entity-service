package com.example.universe.simulator.entityservice.controllers.rest;

import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.filters.PlanetFilter;
import com.example.universe.simulator.entityservice.services.PlanetService;
import com.example.universe.simulator.entityservice.specifications.PlanetSpecificationBuilder;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("planets")
@RequiredArgsConstructor
@Slf4j
public class PlanetRestController {

    private final PlanetService service;
    private final PlanetDtoValidator validator;
    private final PlanetSpecificationBuilder specificationBuilder;
    private final ModelMapper modelMapper;

    @GetMapping
    public Callable<Page<PlanetDto>> getList(@RequestParam(required = false) String name, @ParameterObject Pageable pageable) {
        var filter = PlanetFilter.builder()
            .name(name)
            .build();
        log.info("calling getList with filter [{}] and {}", filter, pageable);
        Specification<Planet> specification = specificationBuilder.build(filter);

        return () -> {
            Page<PlanetDto> result = service.getList(specification, pageable)
                .map(item -> modelMapper.map(item, PlanetDto.class));
            log.info("fetched [{}] record(s)", result.getNumberOfElements());

            return result;
        };
    }

    @GetMapping("{id}")
    public PlanetDto get(@PathVariable UUID id) throws AppException {
        log.info("calling get with id [{}]", id);
        PlanetDto result = modelMapper.map(service.get(id), PlanetDto.class);
        log.info("fetched [{}]", result.getId());

        return result;
    }

    @PostMapping
    public PlanetDto add(@RequestBody PlanetDto dto) throws AppException {
        log.info("calling add with {}, star id [{}]", dto, dto.getStar().getId());
        validator.validate(dto, false);

        Planet entity = modelMapper.map(dto, Planet.class);
        PlanetDto result = modelMapper.map(service.add(entity), PlanetDto.class);
        log.info("added [{}]", result.getId());

        return result;
    }

    @PutMapping
    public PlanetDto update(@RequestBody PlanetDto dto) throws AppException {
        log.info("calling update with {}, star id [{}]", dto, dto.getStar().getId());
        validator.validate(dto, true);

        Planet entity = modelMapper.map(dto, Planet.class);
        PlanetDto result = modelMapper.map(service.update(entity), PlanetDto.class);
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
