package com.example.universe.simulator.entityservice.filters;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter @Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class MoonFilter extends SpaceEntityFilter {}
