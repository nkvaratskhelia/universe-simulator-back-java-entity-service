package com.example.universe.simulator.entityservice.dtos;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter @Setter
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GalaxyDto extends SpaceEntityDto {

    @Override
    void validateDtoFields() {
        // there is no dto-specific validation yet
    }

    @Override
    void fixDtoDirtyFields() {
        // there are no dto-specific fields to fix yet
    }
}
