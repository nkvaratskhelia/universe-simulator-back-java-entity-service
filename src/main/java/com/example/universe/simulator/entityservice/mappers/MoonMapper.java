package com.example.universe.simulator.entityservice.mappers;

import com.example.universe.simulator.entityservice.dtos.MoonDto;
import com.example.universe.simulator.entityservice.entities.Moon;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public interface MoonMapper {

    MoonDto toDto(Moon entity);

    Moon toEntity(MoonDto dto);
}
