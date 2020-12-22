package com.example.universe.simulator.entityservice.controllers;

import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.services.GalaxyService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("galaxy")
@RequiredArgsConstructor
public class GalaxyController {

    private final GalaxyService service;

    @GetMapping("get-list")
    public List<Galaxy> getList() {
        return service.getList();
    }

    @GetMapping("get/{id}")
    public Galaxy get(@PathVariable UUID id) {
        return service.get(id);
    }

    @PostMapping("add")
    public Galaxy add(@RequestBody Galaxy entity) {
        return service.add(entity);
    }

    @PutMapping("update")
    public Galaxy update(@RequestBody Galaxy entity) {
        return service.update(entity);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
