package com.example.universe.simulator.entityservice.dtos;

import com.example.universe.simulator.entityservice.exception.AppException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class StarDto extends SpaceEntityDto {

    private GalaxyDto galaxy;

    @Override
    protected void validateDtoSpecificFields() throws AppException {
    }

    @Override
    protected void fixDtoSpecificDirtyFields() {
    }

}
