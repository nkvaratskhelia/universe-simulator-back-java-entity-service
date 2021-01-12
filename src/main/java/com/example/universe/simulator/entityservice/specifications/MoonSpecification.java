package com.example.universe.simulator.entityservice.specifications;

import com.example.universe.simulator.entityservice.entities.Moon;
import com.example.universe.simulator.entityservice.filters.MoonFilter;
import org.springframework.data.jpa.domain.Specification;

public class MoonSpecification extends SpaceEntitySpecification<Moon, MoonFilter> {

    @Override
    Specification<Moon> getEntitySpecification(MoonFilter filter) {
        return null;
    }
}
