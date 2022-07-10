package com.example.universe.simulator.entityservice.validators;

import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.types.ErrorCodeType;
import org.springframework.stereotype.Component;

@Component
public class StarDtoValidator extends SpaceEntityDtoValidator<StarDto> {

    @Override
    void validateDtoFields(StarDto dto) throws AppException {
        if (dto.getGalaxyId() == null) {
            throw new AppException(ErrorCodeType.MISSING_PARAMETER_GALAXY_ID);
        }
    }

    @Override
    void fixDtoDirtyFields(StarDto dto) {
        // there are no dto-specific fields to fix yet
    }
}
