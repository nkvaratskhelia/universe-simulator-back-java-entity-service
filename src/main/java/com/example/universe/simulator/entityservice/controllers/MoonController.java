package com.example.universe.simulator.entityservice.controllers;

import com.example.universe.simulator.entityservice.dtos.MoonDto;
import com.example.universe.simulator.entityservice.entities.Moon;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.filters.MoonFilter;
import com.example.universe.simulator.entityservice.services.MoonService;
import com.example.universe.simulator.entityservice.specifications.MoonSpecification;
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

@RestController
@RequestMapping("moon")
@RequiredArgsConstructor
public class MoonController {

    private final ModelMapper modelMapper;

    private final MoonService service;

    @PostMapping("get-list")
    private Page<MoonDto> getList(@RequestBody Optional<MoonFilter> filter, Pageable pageable) {
        Specification<Moon> specification = filter.map(item -> new MoonSpecification().getSpecification(item))
                .orElse(null);
        return service.getList(specification, pageable)
                .map(item -> modelMapper.map(item, MoonDto.class));
    }

    @GetMapping("get/{id}")
    private MoonDto get(@PathVariable UUID id) throws AppException {
        return modelMapper.map(service.get(id), MoonDto.class);
    }

    @PostMapping("add")
    private MoonDto add(@RequestBody MoonDto dto) throws AppException {
        dto.validate(false);

        Moon entity = modelMapper.map(dto, Moon.class);
        return modelMapper.map(service.add(entity), MoonDto.class);
    }

    @PutMapping("update")
    private MoonDto update(@RequestBody MoonDto dto) throws AppException {
        dto.validate(true);

        Moon entity = modelMapper.map(dto, Moon.class);
        return modelMapper.map(service.update(entity), MoonDto.class);
    }

    @DeleteMapping("delete/{id}")
    private void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
