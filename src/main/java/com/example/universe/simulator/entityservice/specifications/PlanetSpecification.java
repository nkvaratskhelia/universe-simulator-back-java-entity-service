package com.example.universe.simulator.entityservice.specifications;

import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.filters.PlanetFilter;
import org.springframework.data.jpa.domain.Specification;

public class PlanetSpecification extends SpaceEntitySpecification<Planet, PlanetFilter> {
    @Override
    Specification<Planet> getEntitySpecification(PlanetFilter filter) {
        return null;
    }
}
