package com.example.universe.simulator.entityservice.inputs;

import org.springframework.data.domain.Sort;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public record PageInput(@NotNull Integer page, @NotNull Integer size, @NotNull List<@Valid SortOrder> sort) {
    public record SortOrder(@NotBlank String property, @NotNull Sort.Direction direction) {}
}
