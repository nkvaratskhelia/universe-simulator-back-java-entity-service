package com.example.universe.simulator.entityservice.mappers;

import com.example.universe.simulator.entityservice.controllers.graphql.StarGraphQLController;
import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.entities.Star;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public interface StarMapper {

    StarDto toDto(Star entity);

    Star toEntity(StarDto dto);

    StarDto toDto(StarGraphQLController.UpdateStarInput input);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    StarDto toDto(StarGraphQLController.AddStarInput input);

}
