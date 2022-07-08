package com.example.universe.simulator.entityservice.mappers;

import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.entities.Planet;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class, uses = StarMapper.class)
public interface PlanetMapper {

    PlanetDto toDto(Planet entity);

    Planet toEntity(PlanetDto dto);
}
