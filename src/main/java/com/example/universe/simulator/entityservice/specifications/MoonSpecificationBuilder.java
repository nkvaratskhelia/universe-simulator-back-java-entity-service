package com.example.universe.simulator.entityservice.specifications;

import com.example.universe.simulator.entityservice.entities.Moon;
import com.example.universe.simulator.entityservice.filters.MoonFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class MoonSpecificationBuilder extends SpaceEntitySpecificationBuilder<Moon, MoonFilter> {

    @Override
    Specification<Moon> buildEntitySpecification(MoonFilter filter) {
        return null;
    }
}
