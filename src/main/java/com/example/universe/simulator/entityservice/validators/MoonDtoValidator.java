package com.example.universe.simulator.entityservice.validators;

import com.example.universe.simulator.entityservice.dtos.MoonDto;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.types.ErrorCodeType;
import org.springframework.stereotype.Component;

@Component
public class MoonDtoValidator extends SpaceEntityDtoValidator<MoonDto> {

    @Override
    void validateDtoFields(MoonDto dto) throws AppException {
        if (dto.getPlanetId() == null) {
            throw new AppException(ErrorCodeType.MISSING_PARAMETER_PLANET_ID);
        }
    }

    @Override
    void fixDtoDirtyFields(MoonDto dto) {
        // there are no dto-specific fields to fix yet
    }
}
