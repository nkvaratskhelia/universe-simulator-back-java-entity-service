package com.example.universe.simulator.entityservice.inputs;

import org.springframework.data.domain.Sort;

import java.util.List;

public record PageInput(Integer page, Integer size, List<SortOrder> sort) {
    public record SortOrder(String property, Sort.Direction direction) {}
}
