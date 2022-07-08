package com.example.universe.simulator.entityservice.specifications;

import com.example.universe.simulator.entityservice.entities.SpaceEntity;
import com.example.universe.simulator.entityservice.entities.SpaceEntity_;
import com.example.universe.simulator.entityservice.filters.SpaceEntityFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

abstract class SpaceEntitySpecificationBuilder<E extends SpaceEntity, F extends SpaceEntityFilter> {

    public final Specification<E> build(F filter) {
        return buildCommonSpecification(filter)
            .and(buildEntitySpecification(filter));
    }

    private Specification<E> buildCommonSpecification(F filter) {
        return Specification.where(nameLike(filter.getName()));
    }

    private Specification<E> nameLike(String name) {
        return StringUtils.hasText(name)
               ? AbstractSpecification.like(SpaceEntity_.NAME, name)
               : null;
    }

    abstract Specification<E> buildEntitySpecification(F filter);
}
