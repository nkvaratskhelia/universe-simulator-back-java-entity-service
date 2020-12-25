package com.example.universe.simulator.entityservice.utils;

import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.dtos.PlanetDto;

import java.util.UUID;

public final class TestUtils {

    private TestUtils() {}

    public static GalaxyDto buildGalaxyDtoForAdd() {
        return GalaxyDto.builder()
                .name("name")
                .build();
    }

    public static GalaxyDto buildGalaxyDtoForUpdate() {
        GalaxyDto result = buildGalaxyDtoForAdd();
        result.setId(UUID.randomUUID());
        result.setVersion(0L);

        return result;
    }

    public static PlanetDto buildPlanetDtoForAdd() {
        return PlanetDto.builder()
                .name("name")
                .build();
    }

    public static PlanetDto buildPlanetDtoForUpdate() {
        PlanetDto result = buildPlanetDtoForAdd();
        result.setId(UUID.randomUUID());
        result.setVersion(0L);
        return result;
    }

}
