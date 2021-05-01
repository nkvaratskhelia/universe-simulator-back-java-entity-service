package com.example.universe.simulator.entityservice.validators;

import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class StarDtoValidator extends SpaceEntityDtoValidator<StarDto> {

    @Override
    void validateDtoFields(StarDto dto) throws AppException {
        if (Objects.isNull(dto.getGalaxy())) {
            throw new AppException(ErrorCodeType.MISSING_PARAMETER_GALAXY);
        }
        if (Objects.isNull(dto.getGalaxy().getId())) {
            throw new AppException(ErrorCodeType.MISSING_PARAMETER_GALAXY_ID);
        }
    }

    @Override
    void fixDtoDirtyFields(StarDto dto) {
        // there are no dto-specific fields to fix yet
    }
}
