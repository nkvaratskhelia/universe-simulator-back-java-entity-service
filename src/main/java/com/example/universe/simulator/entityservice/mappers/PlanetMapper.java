package com.example.universe.simulator.entityservice.mappers;

import com.example.universe.simulator.entityservice.controllers.graphql.PlanetGraphQLController;
import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.entities.Planet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public interface PlanetMapper {

    PlanetDto toDto(Planet entity);

    Planet toEntity(PlanetDto dto);

    PlanetDto toDto(PlanetGraphQLController.UpdatePlanetInput input);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    PlanetDto toDto(PlanetGraphQLController.AddPlanetInput input);

}
