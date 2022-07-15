package com.example.universe.simulator.entityservice.common.utils;

import com.example.universe.simulator.common.dtos.EventDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.entities.Moon;
import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.filters.GalaxyFilter;
import com.example.universe.simulator.entityservice.filters.MoonFilter;
import com.example.universe.simulator.entityservice.filters.PlanetFilter;
import com.example.universe.simulator.entityservice.filters.StarFilter;
import com.example.universe.simulator.entityservice.inputs.AddGalaxyInput;
import com.example.universe.simulator.entityservice.inputs.AddMoonInput;
import com.example.universe.simulator.entityservice.inputs.AddPlanetInput;
import com.example.universe.simulator.entityservice.inputs.AddStarInput;
import com.example.universe.simulator.entityservice.inputs.UpdateGalaxyInput;
import com.example.universe.simulator.entityservice.inputs.UpdateMoonInput;
import com.example.universe.simulator.entityservice.inputs.UpdatePlanetInput;
import com.example.universe.simulator.entityservice.inputs.UpdateStarInput;
import com.example.universe.simulator.entityservice.types.EventType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class TestUtils {

    private TestUtils() {
    }

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

    public static Pageable getSpaceEntityPageableWithoutSorting() {
        return PageRequest.of(1, 2, Sort.unsorted());
    }

    public static EventDto buildEventDto(Clock clock) {
        return new EventDto(
            EventType.SPACE_ENTITY_STATISTICS.toString(),
            "data",
            OffsetDateTime.now(clock)
        );
    }

    public static AddGalaxyInput buildAddGalaxyInput() {
        return new AddGalaxyInput("name");
    }

    public static UpdateGalaxyInput buildUpdateGalaxyInput() {
        return new UpdateGalaxyInput(UUID.randomUUID(), 0L, "name");
    }

    public static GalaxyFilter buildGalaxyFilter() {
        return GalaxyFilter.builder()
            .name("name")
            .build();
    }

    public static Galaxy buildGalaxyWithName(String name) {
        return Galaxy.builder()
            .id(UUID.randomUUID())
            .name(name)
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

    public static AddStarInput buildAddStarInput() {
        return new AddStarInput("name", UUID.randomUUID());
    }

    public static UpdateStarInput buildUpdateStarInput() {
        return new UpdateStarInput(UUID.randomUUID(), 0L, "name", UUID.randomUUID());
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

    public static AddPlanetInput buildAddPlanetInput() {
        return new AddPlanetInput("name", UUID.randomUUID());
    }

    public static UpdatePlanetInput buildUpdatePlanetInput() {
        return new UpdatePlanetInput(UUID.randomUUID(), 0L, "name", UUID.randomUUID());
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

    public static AddMoonInput buildAddMoonInput() {
        return new AddMoonInput("name", UUID.randomUUID());
    }

    public static UpdateMoonInput buildUpdateMoonInput() {
        return new UpdateMoonInput(UUID.randomUUID(), 0L, "name", UUID.randomUUID());
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

    public static Map<String, Object> buildInputMapForPagingAndSorting(Pageable pageable) {
        Map sortVersionMap = Map.of(
            "property", "version",
            "direction", Sort.Direction.DESC
        );
        Map sortNameMap = Map.of(
            "property", "name",
            "direction", Sort.Direction.ASC
        );
        return Map.of(
            "page", pageable.getPageNumber(),
            "size", pageable.getPageSize(),
            "sortOrders", List.of(sortVersionMap, sortNameMap));
    }

    public static Map<String, Object> buildInputMapForOnlyPaging(Pageable pageable) {
        return Map.of(
            "page", pageable.getPageNumber(),
            "size", pageable.getPageSize());
    }
}
