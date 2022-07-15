package com.example.universe.simulator.entityservice.inputs;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public record AddPlanetInput(@NotBlank String name, @NotNull UUID starId) {}
