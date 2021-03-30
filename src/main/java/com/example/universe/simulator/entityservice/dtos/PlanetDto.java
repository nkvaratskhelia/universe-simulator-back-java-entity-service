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
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PlanetDto extends SpaceEntityDto {

    @EqualsAndHashCode.Exclude
    private StarDto star;

    @Override
    void validateDtoFields() throws AppException {
        if (Objects.isNull(star)) {
            throw new AppException(ErrorCodeType.MISSING_PARAMETER_STAR);
        }
        if (Objects.isNull(star.getId())) {
            throw new AppException(ErrorCodeType.MISSING_PARAMETER_STAR_ID);
        }
    }

    @Override
    void fixDtoDirtyFields() {}
}
