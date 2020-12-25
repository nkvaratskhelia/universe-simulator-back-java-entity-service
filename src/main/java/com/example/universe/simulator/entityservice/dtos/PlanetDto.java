package com.example.universe.simulator.entityservice.dtos;

import com.example.universe.simulator.entityservice.entities.Moon;
import com.example.universe.simulator.entityservice.entities.Star;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class PlanetDto extends SpaceEntityDto {

    private Star star;

    private Set<Moon> moons;

    @Override
    protected void validateDtoSpecificFields() {
    }

    @Override
    protected void fixDtoSpecificDirtyFields() {
    }

}
