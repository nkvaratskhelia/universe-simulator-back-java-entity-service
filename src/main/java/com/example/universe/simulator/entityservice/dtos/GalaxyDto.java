package com.example.universe.simulator.entityservice.dtos;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@ToString(callSuper = true)
public class GalaxyDto extends SpaceEntityDto {

    @Override
    void validateDtoFields() {}

    @Override
    void fixDtoDirtyFields() {}
}
