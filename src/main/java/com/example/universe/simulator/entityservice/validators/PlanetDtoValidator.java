package com.example.universe.simulator.entityservice.validators;

import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.types.ErrorCodeType;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PlanetDtoValidator extends SpaceEntityDtoValidator<PlanetDto> {

    @Override
    void validateDtoFields(PlanetDto dto) throws AppException {
        if (Objects.isNull(dto.getStar())) {
            throw new AppException(ErrorCodeType.MISSING_PARAMETER_STAR);
        }
        if (Objects.isNull(dto.getStar().getId())) {
            throw new AppException(ErrorCodeType.MISSING_PARAMETER_STAR_ID);
        }
    }

    @Override
    void fixDtoDirtyFields(PlanetDto dto) {
        // TODO it is clear without comment, that there is no implementation yet
        // there are no dto-specific fields to fix yet
    }
}
