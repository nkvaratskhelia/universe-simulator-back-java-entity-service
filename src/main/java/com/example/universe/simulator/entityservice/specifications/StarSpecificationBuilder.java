package com.example.universe.simulator.entityservice.specifications;

import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.filters.StarFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class StarSpecificationBuilder extends SpaceEntitySpecificationBuilder<Star, StarFilter> {

    @Override
    Specification<Star> buildEntitySpecification(StarFilter filter) {
        return null;
    }
}
