package com.example.universe.simulator.entityservice.controllers;

import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.filters.StarFilter;
import com.example.universe.simulator.entityservice.services.StarService;
import com.example.universe.simulator.entityservice.specifications.StarSpecification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
public class StarController {

    private final ModelMapper modelMapper;

    private final StarService service;

    @PostMapping("get-list")
    private Callable<Page<StarDto>> getList(@RequestBody Optional<StarFilter> filter, Pageable pageable) {
        Specification<Star> specification = filter.map(item -> new StarSpecification().getSpecification(item))
                .orElse(null);
        return () -> service.getList(specification, pageable)
                .map(item -> modelMapper.map(item, StarDto.class));
    }

    @GetMapping("get/{id}")
    private StarDto get(@PathVariable UUID id) throws AppException {
        return modelMapper.map(service.get(id), StarDto.class);
    }

    @PostMapping("add")
    private StarDto add(@RequestBody StarDto dto) throws AppException {
        dto.validate(false);

        Star entity = modelMapper.map(dto, Star.class);
        return modelMapper.map(service.add(entity), StarDto.class);
    }

    @PutMapping("update")
    private StarDto update(@RequestBody StarDto dto) throws AppException {
        dto.validate(true);

        Star entity = modelMapper.map(dto, Star.class);
        return modelMapper.map(service.update(entity), StarDto.class);
    }

    @DeleteMapping("delete/{id}")
    private void delete(@PathVariable UUID id) throws AppException {
        service.delete(id);
    }
}
