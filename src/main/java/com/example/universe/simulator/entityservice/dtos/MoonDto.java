package com.example.universe.simulator.entityservice.dtos;

import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@ToString(callSuper = true)
public class MoonDto extends SpaceEntityDto {

    @EqualsAndHashCode.Exclude
    private PlanetDto planet;

    @Override
    void validateDtoFields() throws AppException {
        if (Objects.isNull(planet)) {
            throw new AppException(ErrorCodeType.MISSING_PARAMETER_PLANET);
        }
        if (Objects.isNull(planet.getId())) {
            throw new AppException(ErrorCodeType.MISSING_PARAMETER_PLANET_ID);
        }
    }

    @Override
    void fixDtoDirtyFields() {}
}
