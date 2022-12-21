package com.example.universe.simulator.entityservice.inputs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateMoonInput(@NotNull UUID id, @NotNull Long version, @NotBlank String name, @NotNull UUID planetId) {}
