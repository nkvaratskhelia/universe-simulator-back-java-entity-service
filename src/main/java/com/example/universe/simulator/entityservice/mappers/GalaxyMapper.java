package com.example.universe.simulator.entityservice.mappers;

import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public interface GalaxyMapper {

    GalaxyDto toDto(Galaxy entity);

    Galaxy toEntity(GalaxyDto dto);
}
