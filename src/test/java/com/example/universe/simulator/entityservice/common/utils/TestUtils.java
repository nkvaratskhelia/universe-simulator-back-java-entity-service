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
import com.example.universe.simulator.entityservice.inputs.PageInput;
import com.example.universe.simulator.entityservice.inputs.UpdateGalaxyInput;
import com.example.universe.simulator.entityservice.inputs.UpdateMoonInput;
import com.example.universe.simulator.entityservice.inputs.UpdatePlanetInput;
import com.example.universe.simulator.entityservice.inputs.UpdateStarInput;
import com.example.universe.simulator.entityservice.types.EventType;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@UtilityClass
public class TestUtils {

    public final int DEFAULT_PAGE_NUMBER = 0;
    public final int DEFAULT_PAGE_SIZE = 20;
    public final Sort DEFAULT_SORT = Sort.unsorted();

    public Pageable buildDefaultPageable() {
        return PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, DEFAULT_SORT);
    }

    public Pageable buildSpaceEntityPageable() {
        Sort sort = Sort.by(
            Sort.Order.desc("version"),
            Sort.Order.asc("name")
        );
        return PageRequest.of(1, 2, sort);
    }

    public EventDto buildEventDto(Clock clock) {
        return new EventDto(
            EventType.SPACE_ENTITY_STATISTICS.toString(),
            "data",
            OffsetDateTime.now(clock)
        );
    }

    public PageInput buildDefaultPageInput() {
        return new PageInput(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, List.of());
    }

    public AddGalaxyInput buildAddGalaxyInput() {
        return new AddGalaxyInput("name");
    }

    public UpdateGalaxyInput buildUpdateGalaxyInput() {
        return new UpdateGalaxyInput(UUID.randomUUID(), 0L, "name");
    }

    public GalaxyFilter buildGalaxyFilter() {
        return GalaxyFilter.builder()
            .name("name")
            .build();
    }

    public Galaxy buildGalaxy() {
        return Galaxy.builder()
            .id(UUID.randomUUID())
            .version(0L)
            .name("name")
            .build();
    }

    public Galaxy buildGalaxyForAdd() {
        return Galaxy.builder()
            .name("name")
            .build();
    }

    public AddStarInput buildAddStarInput() {
        return new AddStarInput("name", UUID.randomUUID());
    }

    public UpdateStarInput buildUpdateStarInput() {
        return new UpdateStarInput(UUID.randomUUID(), 0L, "name", UUID.randomUUID());
    }

    public StarFilter buildStarFilter() {
        return StarFilter.builder()
            .name("name")
            .build();
    }

    public Star buildStar() {
        return Star.builder()
            .id(UUID.randomUUID())
            .name("name")
            .galaxyId(UUID.randomUUID())
            .build();
    }

    public Star buildStarForAdd() {
        return Star.builder()
            .name("name")
            .galaxyId(UUID.randomUUID())
            .build();
    }

    public AddPlanetInput buildAddPlanetInput() {
        return new AddPlanetInput("name", UUID.randomUUID());
    }

    public UpdatePlanetInput buildUpdatePlanetInput() {
        return new UpdatePlanetInput(UUID.randomUUID(), 0L, "name", UUID.randomUUID());
    }

    public PlanetFilter buildPlanetFilter() {
        return PlanetFilter.builder()
            .name("name")
            .build();
    }

    public Planet buildPlanet() {
        return Planet.builder()
            .id(UUID.randomUUID())
            .name("name")
            .starId(UUID.randomUUID())
            .build();
    }

    public Planet buildPlanetForAdd() {
        return Planet.builder()
            .name("name")
            .starId(UUID.randomUUID())
            .build();
    }

    public AddMoonInput buildAddMoonInput() {
        return new AddMoonInput("name", UUID.randomUUID());
    }

    public UpdateMoonInput buildUpdateMoonInput() {
        return new UpdateMoonInput(UUID.randomUUID(), 0L, "name", UUID.randomUUID());
    }

    public MoonFilter buildMoonFilter() {
        return MoonFilter.builder()
            .name("name")
            .build();
    }

    public Moon buildMoon() {
        return Moon.builder()
            .id(UUID.randomUUID())
            .name("name")
            .planetId(UUID.randomUUID())
            .build();
    }

    public static Map<String, Object> buildInputMapForGalaxyAdd(AddGalaxyInput input) {
        return Map.of("name", input.name());
    }

    public static Map<String, Object> buildInputMapForGalaxyUpdate(UpdateGalaxyInput input) {
        return Map.of(
            "id", input.id(),
            "name", input.name(),
            "version", input.version());
    }

    public static Map<String, Object> buildInputMapForStarAdd(AddStarInput input) {
        return Map.of(
            "name", input.name(),
            "galaxyId", input.galaxyId());
    }

    public static Map<String, Object> buildInputMapForStarUpdate(UpdateStarInput input) {
        return Map.of(
            "id", input.id(),
            "name", input.name(),
            "version", input.version(),
            "galaxyId", input.galaxyId());
    }

    public static Map<String, Object> buildInputMapForPlanetAdd(AddPlanetInput input) {
        return Map.of(
            "name", input.name(),
            "starId", input.starId());
    }

    public static Map<String, Object> buildInputMapForPlanetUpdate(UpdatePlanetInput input) {
        return Map.of(
            "id", input.id(),
            "name", input.name(),
            "version", input.version(),
            "starId", input.starId());
    }

    public static Map<String, Object> buildInputMapForMoonAdd(AddMoonInput input) {
        return Map.of(
            "name", input.name(),
            "planetId", input.planetId());
    }

    public static Map<String, Object> buildInputMapForMoonUpdate(UpdateMoonInput input) {
        return Map.of(
            "id", input.id(),
            "name", input.name(),
            "version", input.version(),
            "planetId", input.planetId());
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
            "sort", List.of(sortVersionMap, sortNameMap));
    }
}
