package com.example.universe.simulator.entityservice.utils;

import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.dtos.MoonDto;
import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.dtos.StarDto;

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

    public static StarDto buildStarDtoForAdd() {
        return StarDto.builder()
                .name("name")
                .galaxy(GalaxyDto.builder().id(UUID.randomUUID()).build())
                .build();
    }

    public static StarDto buildStarDtoForUpdate() {
        StarDto result = buildStarDtoForAdd();
        result.setId(UUID.randomUUID());
        result.setVersion(0L);

        return result;
    }

    public static PlanetDto buildPlanetDtoForAdd() {
        return PlanetDto.builder()
                .name("name")
                .star(StarDto.builder().id(UUID.randomUUID()).build())
                .build();
    }

    public static PlanetDto buildPlanetDtoForUpdate() {
        PlanetDto result = buildPlanetDtoForAdd();
        result.setId(UUID.randomUUID());
        result.setVersion(0L);
        
        return result;
    }

    public static MoonDto buildMoonDtoForAdd() {
        return MoonDto.builder()
                .name("name")
                .planet(PlanetDto.builder().id(UUID.randomUUID()).build())
                .build();
    }

    public static MoonDto buildMoonDtoForUpdate() {
        MoonDto result = buildMoonDtoForAdd();
        result.setId(UUID.randomUUID());
        result.setVersion(0L);

        return result;
    }
}
