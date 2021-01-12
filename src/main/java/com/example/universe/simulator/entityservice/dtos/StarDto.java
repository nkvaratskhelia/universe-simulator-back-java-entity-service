package com.example.universe.simulator.entityservice.dtos;

import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class StarDto extends SpaceEntityDto {

    @EqualsAndHashCode.Exclude
    private GalaxyDto galaxy;

    @Override
    void validateDtoFields() throws AppException {
        if (Objects.isNull(galaxy)) {
            throw new AppException(ErrorCodeType.MISSING_PARAMETER_GALAXY);
        }
        if (Objects.isNull(galaxy.getId())) {
            throw new AppException(ErrorCodeType.MISSING_PARAMETER_GALAXY_ID);
        }
    }

    @Override
    void fixDtoDirtyFields() {}
}
