package com.example.universe.simulator.entityservice.common.utils;

import com.example.universe.simulator.common.events.Event;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.dtos.MoonDto;
import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.entities.Moon;
import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.types.EventType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.UUID;

public final class TestUtils {

    private TestUtils() {}

    public static Pageable getDefaultPageable() {
        return PageRequest.of(0, 20, Sort.unsorted());
    }

    public static Pageable getSpaceEntityPageable() {
        Sort sort = Sort.by(
            Sort.Order.desc("version"),
            Sort.Order.asc("name")
        );
        return PageRequest.of(1, 2, sort);
    }

    public static Event buildEvent() {
        return Event.builder()
            .type(EventType.SPACE_ENTITY_STATISTICS.toString())
            .data("data")
            .build();
    }

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

    public static Galaxy buildGalaxy() {
        return Galaxy.builder()
            .id(UUID.randomUUID())
            .name("name")
            .build();
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

    public static Star buildStar() {
        return Star.builder()
            .id(UUID.randomUUID())
            .name("name")
            .galaxy(Galaxy.builder().id(UUID.randomUUID()).build())
            .build();
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

    public static Planet buildPlanet() {
        return Planet.builder()
            .id(UUID.randomUUID())
            .name("name")
            .star(Star.builder().id(UUID.randomUUID()).build())
            .build();
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

    public static Moon buildMoon() {
        return Moon.builder()
            .id(UUID.randomUUID())
            .name("name")
            .planet(Planet.builder().id(UUID.randomUUID()).build())
            .build();
    }
}
