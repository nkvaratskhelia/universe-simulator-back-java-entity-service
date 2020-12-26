package com.example.universe.simulator.entityservice.dtos;

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
public class PlanetDto extends SpaceEntityDto {

    private StarDto star;

    @Override
    protected void validateDtoSpecificFields() {
    }

    @Override
    protected void fixDtoSpecificDirtyFields() {
    }

}
