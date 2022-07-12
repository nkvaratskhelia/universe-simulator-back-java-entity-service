package com.example.universe.simulator.entityservice.common.utils;

import com.example.universe.simulator.common.dtos.EventDto;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.dtos.MoonDto;
import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.entities.Moon;
import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.filters.GalaxyFilter;
import com.example.universe.simulator.entityservice.filters.MoonFilter;
import com.example.universe.simulator.entityservice.filters.PlanetFilter;
import com.example.universe.simulator.entityservice.filters.StarFilter;
import com.example.universe.simulator.entityservice.types.EventType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Map;
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

    public static EventDto buildEventDto(Clock clock) {
        return new EventDto(
            EventType.SPACE_ENTITY_STATISTICS.toString(),
            "data",
            OffsetDateTime.now(clock)
        );
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

    public static GalaxyFilter buildGalaxyFilter() {
        return GalaxyFilter.builder()
            .name("name")
            .build();
    }

    public static Galaxy buildGalaxy() {
        return Galaxy.builder()
            .id(UUID.randomUUID())
            .name("name")
            .build();
    }

    public static Galaxy buildGalaxyForAdd() {
        return Galaxy.builder()
            .name("name")
            .build();
    }

    public static StarDto buildStarDtoForAdd() {
        return StarDto.builder()
            .name("name")
            .galaxyId(UUID.randomUUID())
            .build();
    }

    public static StarDto buildStarDtoForUpdate() {
        StarDto result = buildStarDtoForAdd();
        result.setId(UUID.randomUUID());
        result.setVersion(0L);

        return result;
    }

    public static StarFilter buildStarFilter() {
        return StarFilter.builder()
            .name("name")
            .build();
    }

    public static Star buildStar() {
        return Star.builder()
            .id(UUID.randomUUID())
            .name("name")
            .galaxyId(UUID.randomUUID())
            .build();
    }

    public static Star buildStarForAdd() {
        return Star.builder()
            .name("name")
            .galaxyId(UUID.randomUUID())
            .build();
    }

    public static PlanetDto buildPlanetDtoForAdd() {
        return PlanetDto.builder()
            .name("name")
            .starId(UUID.randomUUID())
            .build();
    }

    public static PlanetDto buildPlanetDtoForUpdate() {
        PlanetDto result = buildPlanetDtoForAdd();
        result.setId(UUID.randomUUID());
        result.setVersion(0L);

        return result;
    }

    public static PlanetFilter buildPlanetFilter() {
        return PlanetFilter.builder()
            .name("name")
            .build();
    }

    public static Planet buildPlanet() {
        return Planet.builder()
            .id(UUID.randomUUID())
            .name("name")
            .starId(UUID.randomUUID())
            .build();
    }

    public static Planet buildPlanetForAdd() {
        return Planet.builder()
            .name("name")
            .starId(UUID.randomUUID())
            .build();
    }

    public static MoonDto buildMoonDtoForAdd() {
        return MoonDto.builder()
            .name("name")
            .planetId(UUID.randomUUID())
            .build();
    }

    public static MoonDto buildMoonDtoForUpdate() {
        MoonDto result = buildMoonDtoForAdd();
        result.setId(UUID.randomUUID());
        result.setVersion(0L);

        return result;
    }

    public static MoonFilter buildMoonFilter() {
        return MoonFilter.builder()
            .name("name")
            .build();
    }

    public static Moon buildMoon() {
        return Moon.builder()
            .id(UUID.randomUUID())
            .name("name")
            .planetId(UUID.randomUUID())
            .build();
    }

    public static Map<String, Object> buildInputMapForGalaxyAdd(GalaxyDto dto) {
        return Map.of("name", dto.getName());
    }

    public static Map<String, Object> buildInputMapForGalaxyUpdate(GalaxyDto dto) {
        return Map.of(
            "id", dto.getId(),
            "name", dto.getName(),
            "version", dto.getVersion());
    }

    public static Map<String, Object> buildInputMapForStarAdd(StarDto dto) {
        return Map.of(
            "name", dto.getName(),
            "galaxyId", dto.getGalaxyId());
    }

    public static Map<String, Object> buildInputMapForStarUpdate(StarDto dto) {
        return Map.of(
            "id", dto.getId(),
            "name", dto.getName(),
            "version", dto.getVersion(),
            "galaxyId", dto.getGalaxyId());
    }

    public static Map<String, Object> buildInputMapForPlanetAdd(PlanetDto dto) {
        return Map.of(
            "name", dto.getName(),
            "starId", dto.getStarId());
    }

    public static Map<String, Object> buildInputMapForPlanetUpdate(PlanetDto dto) {
        return Map.of(
            "id", dto.getId(),
            "name", dto.getName(),
            "version", dto.getVersion(),
            "starId", dto.getStarId());
    }

    public static Map<String, Object> buildInputMapForMoonAdd(MoonDto dto) {
        return Map.of(
            "name", dto.getName(),
            "planetId", dto.getPlanetId());
    }

    public static Map<String, Object> buildInputMapForMoonUpdate(MoonDto dto) {
        return Map.of(
            "id", dto.getId(),
            "name", dto.getName(),
            "version", dto.getVersion(),
            "planetId", dto.getPlanetId());
    }
}
