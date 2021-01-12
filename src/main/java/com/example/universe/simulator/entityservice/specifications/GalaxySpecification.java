package com.example.universe.simulator.entityservice.specifications;

import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.filters.GalaxyFilter;
import org.springframework.data.jpa.domain.Specification;

public class GalaxySpecification extends SpaceEntitySpecification<Galaxy, GalaxyFilter> {

    @Override
    Specification<Galaxy> getEntitySpecification(GalaxyFilter filter) {
        return null;
    }
}
