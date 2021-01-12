package com.example.universe.simulator.entityservice.specifications;

import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.filters.StarFilter;
import org.springframework.data.jpa.domain.Specification;

public class StarSpecification extends SpaceEntitySpecification<Star, StarFilter> {

    @Override
    Specification<Star> getEntitySpecification(StarFilter filter) {
        return null;
    }
}
