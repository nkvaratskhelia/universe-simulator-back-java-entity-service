package com.example.universe.simulator.entityservice.specifications;

import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.filters.GalaxyFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class GalaxySpecificationBuilder extends SpaceEntitySpecificationBuilder<Galaxy, GalaxyFilter> {

    @Override
    Specification<Galaxy> buildEntitySpecification(GalaxyFilter filter) {
        return null;
    }
}
