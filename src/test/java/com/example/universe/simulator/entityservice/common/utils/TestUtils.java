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
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.UUID;

@UtilityClass
public class TestUtils {

    public int DEFAULT_PAGE_NUMBER = 0;
    public int DEFAULT_PAGE_SIZE = 20;
    public Sort DEFAULT_SORT = Sort.unsorted();

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
}
