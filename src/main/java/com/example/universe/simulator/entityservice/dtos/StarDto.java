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
public class StarDto extends SpaceEntityDto {

    // TODO
    // why the field is excluded in hash and equals and not excluded in toString?
    // it's more a question than urge to refactor
    @EqualsAndHashCode.Exclude
    private GalaxyDto galaxy;
}
