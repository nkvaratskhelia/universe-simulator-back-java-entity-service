package com.example.universe.simulator.entityservice.mappers;

import com.example.universe.simulator.entityservice.controllers.graphql.GalaxyGraphQLController;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public interface GalaxyMapper {

    GalaxyDto toDto(Galaxy entity);

    Galaxy toEntity(GalaxyDto dto);

    GalaxyDto toDto(GalaxyGraphQLController.UpdateGalaxyInput input);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    GalaxyDto toDto(GalaxyGraphQLController.AddGalaxyInput input);

}
