package com.example.universe.simulator.entityservice.specifications;

import com.example.universe.simulator.entityservice.entities.SpaceEntity;
import com.example.universe.simulator.entityservice.entities.SpaceEntity_;
import com.example.universe.simulator.entityservice.filters.SpaceEntityFilter;
import com.example.universe.simulator.entityservice.utils.Utils;
import org.springframework.data.jpa.domain.Specification;

abstract class SpaceEntitySpecification<E extends SpaceEntity, F extends SpaceEntityFilter> {

    public final Specification<E> getSpecification(F filter) {
        return getCommonSpecification(filter)
            .and(getEntitySpecification(filter));
    }

    private Specification<E> getCommonSpecification(F filter) {
        return Specification.where(nameLike(filter.getName()));
    }

    private Specification<E> nameLike(String name) {
        return !Utils.isNullOrBlank(name)
            ? AbstractSpecification.like(SpaceEntity_.NAME, name)
            : null;
    }

    abstract Specification<E> getEntitySpecification(F filter);
}
