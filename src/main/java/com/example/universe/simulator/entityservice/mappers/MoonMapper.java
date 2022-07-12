package com.example.universe.simulator.entityservice.mappers;

import com.example.universe.simulator.entityservice.controllers.graphql.MoonGraphQLController;
import com.example.universe.simulator.entityservice.dtos.MoonDto;
import com.example.universe.simulator.entityservice.entities.Moon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public interface MoonMapper {

    MoonDto toDto(Moon entity);

    Moon toEntity(MoonDto dto);

    MoonDto toDto(MoonGraphQLController.UpdateMoonInput input);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    MoonDto toDto(MoonGraphQLController.AddMoonInput input);

}
