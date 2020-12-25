package com.example.universe.simulator.entityservice.utils;

import com.example.universe.simulator.entityservice.dtos.GalaxyDto;

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
}
