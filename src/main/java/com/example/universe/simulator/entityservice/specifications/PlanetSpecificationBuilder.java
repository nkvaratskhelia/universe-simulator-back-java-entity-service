package com.example.universe.simulator.entityservice.specifications;

import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.filters.PlanetFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PlanetSpecificationBuilder extends SpaceEntitySpecificationBuilder<Planet, PlanetFilter> {

    @Override
    Specification<Planet> buildEntitySpecification(PlanetFilter filter) {
        return null;
    }
}
