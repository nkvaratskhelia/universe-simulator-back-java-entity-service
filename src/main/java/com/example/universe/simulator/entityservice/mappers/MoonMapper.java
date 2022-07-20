package com.example.universe.simulator.entityservice.mappers;

import com.example.universe.simulator.entityservice.dtos.MoonDto;
import com.example.universe.simulator.entityservice.entities.Moon;
import com.example.universe.simulator.entityservice.inputs.AddMoonInput;
import com.example.universe.simulator.entityservice.inputs.UpdateMoonInput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MappingConfig.class)
public interface MoonMapper {

    @Mapping(target = "name", expression = "java(input.name().strip())")
    Moon toEntity(AddMoonInput input);

    @Mapping(target = "name", expression = "java(input.name().strip())")
    Moon toEntity(UpdateMoonInput input);

    MoonDto toDto(Moon entity);

    List<MoonDto> toDtoList(List<Moon> entities);
}
