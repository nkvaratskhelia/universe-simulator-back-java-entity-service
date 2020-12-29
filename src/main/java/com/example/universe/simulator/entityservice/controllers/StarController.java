package com.example.universe.simulator.entityservice.controllers;

import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.services.StarService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("star")
@RequiredArgsConstructor
public class StarController {

    private final ModelMapper modelMapper;

    private final StarService service;

    @GetMapping("get-list")
    private List<StarDto> getList() {
        return modelMapper.map(service.getList(), new TypeToken<List<StarDto>>() {}.getType());
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
    private void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
