package com.example.universe.simulator.entityservice.validators;

import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import org.springframework.stereotype.Component;

@Component
public class GalaxyDtoValidator extends SpaceEntityDtoValidator<GalaxyDto> {

    @Override
    void validateDtoFields(GalaxyDto dto) {
        // TODO it is clear without comment, that there is no implementation yet
        // there is no dto-specific validation yet
    }

    @Override
    void fixDtoDirtyFields(GalaxyDto dto) {
        // TODO it is clear without comment, that there is no implementation yet
        // there are no dto-specific fields to fix yet
    }
}
