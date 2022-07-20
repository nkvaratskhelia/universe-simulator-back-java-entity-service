package com.example.universe.simulator.entityservice.mappers;

import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.inputs.AddPlanetInput;
import com.example.universe.simulator.entityservice.inputs.UpdatePlanetInput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MappingConfig.class)
public interface PlanetMapper {

    @Mapping(target = "name", expression = "java(input.name().strip())")
    Planet toEntity(AddPlanetInput input);

    @Mapping(target = "name", expression = "java(input.name().strip())")
    Planet toEntity(UpdatePlanetInput input);

    PlanetDto toDto(Planet entity);

    List<PlanetDto> toDtoList(List<Planet> entities);
}
