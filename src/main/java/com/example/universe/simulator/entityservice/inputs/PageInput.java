package com.example.universe.simulator.entityservice.inputs;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Sort;

import java.util.List;

public record PageInput(@NotNull Integer page, @NotNull Integer size, @NotNull List<@Valid SortOrder> sort) {
    public record SortOrder(@NotBlank String property, @NotNull Sort.Direction direction) {}
}
