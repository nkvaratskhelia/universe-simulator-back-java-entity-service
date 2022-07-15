package com.example.universe.simulator.entityservice.inputs;

import javax.validation.constraints.NotBlank;

public record AddGalaxyInput(@NotBlank String name) {}
