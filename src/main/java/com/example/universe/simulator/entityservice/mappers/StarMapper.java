package com.example.universe.simulator.entityservice.mappers;

import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.inputs.AddStarInput;
import com.example.universe.simulator.entityservice.inputs.UpdateStarInput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public interface StarMapper {

    @Mapping(target = "name", expression = "java(input.name().strip())")
    Star toEntity(AddStarInput input);

    @Mapping(target = "name", expression = "java(input.name().strip())")
    Star toEntity(UpdateStarInput input);

    StarDto toDto(Star entity);
}
