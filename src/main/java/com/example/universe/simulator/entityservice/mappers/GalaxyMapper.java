package com.example.universe.simulator.entityservice.mappers;

import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.inputs.AddGalaxyInput;
import com.example.universe.simulator.entityservice.inputs.UpdateGalaxyInput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MappingConfig.class)
public interface GalaxyMapper {

    @Mapping(target = "name", expression = "java(input.name().strip())")
    Galaxy toEntity(AddGalaxyInput input);

    @Mapping(target = "name", expression = "java(input.name().strip())")
    Galaxy toEntity(UpdateGalaxyInput input);

    GalaxyDto toDto(Galaxy entity);

    List<GalaxyDto> toDtoList(List<Galaxy> entities);
}
