package com.example.universe.simulator.entityservice.inputs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddStarInput(@NotBlank String name, @NotNull UUID galaxyId) {}
