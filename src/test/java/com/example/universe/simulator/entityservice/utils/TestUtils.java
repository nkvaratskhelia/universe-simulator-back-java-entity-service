package com.example.universe.simulator.entityservice.utils;

import com.example.universe.simulator.entityservice.dtos.GalaxyDto;

import java.util.UUID;

public class TestUtils {

    private TestUtils() {}

    public static GalaxyDto buildSampleGalaxyDtoForAdd() {
        return GalaxyDto.builder()
                .name("name")
                .build();
    }

    public static GalaxyDto buildSampleGalaxyDtoForUpdate() {
        GalaxyDto result = buildSampleGalaxyDtoForAdd();
        result.setId(UUID.randomUUID());
        result.setVersion(0L);

        return result;
    }
}
