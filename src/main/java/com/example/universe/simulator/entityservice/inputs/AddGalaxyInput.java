package com.example.universe.simulator.entityservice.inputs;

import jakarta.validation.constraints.NotBlank;

public record AddGalaxyInput(@NotBlank String name) {}
