package com.example.universe.simulator.entityservice.controllers;

import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.filters.StarFilter;
import com.example.universe.simulator.entityservice.services.StarService;
import com.example.universe.simulator.entityservice.specifications.StarSpecification;
import com.example.universe.simulator.entityservice.validators.StarDtoValidator;
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
@RequestMapping("star")
@RequiredArgsConstructor
@Slf4j
public class StarController {

    private final StarService service;
    private final StarDtoValidator validator;
    private final ModelMapper modelMapper;

    @PostMapping("get-list")
    public Callable<Page<StarDto>> getList(@RequestBody Optional<StarFilter> filter, @ParameterObject Pageable pageable) {
        log.info("calling getList with filter [{}] and {}", filter.orElse(null), pageable);

        Specification<Star> specification = filter
            .map(item -> new StarSpecification().getSpecification(item))
            .orElse(null);

        return () -> {
            Page<StarDto> result = service.getList(specification, pageable)
                .map(item -> modelMapper.map(item, StarDto.class));
            log.info("fetched [{}] record(s)", result.getNumberOfElements());

            return result;
        };
    }

    @GetMapping("get/{id}")
    public StarDto get(@PathVariable UUID id) throws AppException {
        log.info("calling get with id [{}]", id);
        StarDto result = modelMapper.map(service.get(id), StarDto.class);
        log.info("fetched [{}]", result.getId());

        return result;
    }

    @PostMapping("add")
    public StarDto add(@RequestBody StarDto dto) throws AppException {
        log.info("calling add with {}", dto);
        validator.validate(dto, false);

        Star entity = modelMapper.map(dto, Star.class);
        StarDto result = modelMapper.map(service.add(entity), StarDto.class);
        log.info("added [{}]", result.getId());

        return result;
    }

    @PutMapping("update")
    public StarDto update(@RequestBody StarDto dto) throws AppException {
        log.info("calling update with {}", dto);
        validator.validate(dto, true);

        Star entity = modelMapper.map(dto, Star.class);
        StarDto result = modelMapper.map(service.update(entity), StarDto.class);
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
