package com.example.universe.simulator.entityservice.specifications;

import com.example.universe.simulator.entityservice.entities.SpaceEntity;
import com.example.universe.simulator.entityservice.entities.SpaceEntity_;
import com.example.universe.simulator.entityservice.filters.SpaceEntityFilter;
import com.example.universe.simulator.entityservice.utils.Utils;
import org.springframework.data.jpa.domain.Specification;

abstract class SpaceEntitySpecificationBuilder<E extends SpaceEntity, F extends SpaceEntityFilter> {

    public final Specification<E> build(F filter) {
        return buildCommonSpecification(filter)
            .and(buildEntitySpecification(filter));
    }

    private Specification<E> buildCommonSpecification(F filter) {
        return Specification.where(nameLike(filter.getName()));
    }

    private Specification<E> nameLike(String name) {
        return !Utils.isNullOrBlank(name)
               ? AbstractSpecifications.like(SpaceEntity_.NAME, name)
               : null;
    }

    abstract Specification<E> buildEntitySpecification(F filter);
}
