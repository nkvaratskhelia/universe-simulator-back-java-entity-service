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

    private final int DEFAULT_PAGE_NUMBER = 0;
    private final int DEFAULT_PAGE_SIZE = 20;

    public Pageable buildDefaultPageable() {
        return PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, Sort.unsorted());
    }

    public PageInput buildDefaultPageInput() {
        return new PageInput(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, List.of());
    }

    public Pageable buildDefaultSortDirectionPageable() {
        var sort = Sort.by(
            Sort.Order.by("property").with(Sort.DEFAULT_DIRECTION)
        );
        return PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, sort);
    }

    public PageInput buildDefaultSortDirectionPageInput() {
        var sort = List.of(
            new PageInput.SortOrder("property", Sort.DEFAULT_DIRECTION)
        );
        return new PageInput(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, sort);
    }

    public Map<String, Object> buildDefaultSortDirectionPageInputMap() {
        var sort = Map.<String, Object>of("property", "property");
        return Map.of("sort", List.of(sort));
    }

    public Pageable buildSpaceEntityPageable() {
        var sort = Sort.by(
            Sort.Order.desc("version"),
            Sort.Order.asc("name")
        );
        return PageRequest.of(1, 2, sort);
    }

    public PageInput buildSpaceEntityPageInput() {
        var sort = List.of(
            new PageInput.SortOrder("version", Sort.Direction.DESC),
            new PageInput.SortOrder("name", Sort.Direction.ASC)
        );
        return new PageInput(1, 2, sort);
    }

    public Map<String, Object> buildSpaceEntityPageInputMap() {
        var versionSort = Map.<String, Object>of(
            "property", "version",
            "direction", Sort.Direction.DESC
        );
        var nameSort = Map.<String, Object>of(
            "property", "name",
            "direction", Sort.Direction.ASC
        );

        return Map.of(
            "page", 1,
            "size", 2,
            "sort", List.of(versionSort, nameSort)
        );
    }

    public AddGalaxyInput buildAddGalaxyInput() {
        return new AddGalaxyInput("name");
    }

    public Map<String, Object> buildAddGalaxyInputMap(AddGalaxyInput input) {
        return Map.of("name", input.name());
    }

    public UpdateGalaxyInput buildUpdateGalaxyInput() {
        return new UpdateGalaxyInput(UUID.randomUUID(), 0L, "name");
    }

    public Map<String, Object> buildUpdateGalaxyInputMap(UpdateGalaxyInput input) {
        return Map.of(
            "id", input.id(),
            "version", input.version(),
            "name", input.name()
        );
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

    public Map<String, Object> buildAddStarInputMap(AddStarInput input) {
        return Map.of(
            "name", input.name(),
            "galaxyId", input.galaxyId()
        );
    }

    public UpdateStarInput buildUpdateStarInput() {
        return new UpdateStarInput(UUID.randomUUID(), 0L, "name", UUID.randomUUID());
    }

    public Map<String, Object> buildUpdateStarInputMap(UpdateStarInput input) {
        return Map.of(
            "id", input.id(),
            "version", input.version(),
            "name", input.name(),
            "galaxyId", input.galaxyId()
        );
    }

    public StarFilter buildStarFilter() {
        return StarFilter.builder()
            .name("name")
            .build();
    }

    public Star buildStar() {
        return Star.builder()
            .id(UUID.randomUUID())
            .version(0L)
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

    public Map<String, Object> buildAddPlanetInputMap(AddPlanetInput input) {
        return Map.of(
            "name", input.name(),
            "starId", input.starId()
        );
    }

    public UpdatePlanetInput buildUpdatePlanetInput() {
        return new UpdatePlanetInput(UUID.randomUUID(), 0L, "name", UUID.randomUUID());
    }

    public Map<String, Object> buildUpdatePlanetInputMap(UpdatePlanetInput input) {
        return Map.of(
            "id", input.id(),
            "version", input.version(),
            "name", input.name(),
            "starId", input.starId()
        );
    }

    public PlanetFilter buildPlanetFilter() {
        return PlanetFilter.builder()
            .name("name")
            .build();
    }

    public Planet buildPlanet() {
        return Planet.builder()
            .id(UUID.randomUUID())
            .version(0L)
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

    public Map<String, Object> buildAddMoonInputMap(AddMoonInput input) {
        return Map.of(
            "name", input.name(),
            "planetId", input.planetId()
        );
    }

    public UpdateMoonInput buildUpdateMoonInput() {
        return new UpdateMoonInput(UUID.randomUUID(), 0L, "name", UUID.randomUUID());
    }

    public Map<String, Object> buildUpdateMoonInputMap(UpdateMoonInput input) {
        return Map.of(
            "id", input.id(),
            "version", input.version(),
            "name", input.name(),
            "planetId", input.planetId()
        );
    }

    public MoonFilter buildMoonFilter() {
        return MoonFilter.builder()
            .name("name")
            .build();
    }

    public Moon buildMoon() {
        return Moon.builder()
            .id(UUID.randomUUID())
            .version(0L)
            .name("name")
            .planetId(UUID.randomUUID())
            .build();
    }

    public EventDto buildEventDto(Clock clock) {
        return new EventDto(
            EventType.SPACE_ENTITY_STATISTICS.toString(),
            "data",
            OffsetDateTime.now(clock)
        );
    }
}
