package com.example.universe.simulator.entityservice.mappers;

import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.entities.Star;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class, uses = GalaxyMapper.class)
public interface StarMapper {

    StarDto toDto(Star entity);

    Star toEntity(StarDto dto);
}
