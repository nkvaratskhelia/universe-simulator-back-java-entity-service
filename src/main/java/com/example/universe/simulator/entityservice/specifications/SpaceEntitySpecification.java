package com.example.universe.simulator.entityservice.specifications;

import com.example.universe.simulator.entityservice.entities.SpaceEntity_;
import com.example.universe.simulator.entityservice.filters.SpaceEntityFilter;
import com.example.universe.simulator.entityservice.utils.Utils;
import org.springframework.data.jpa.domain.Specification;

abstract class SpaceEntitySpecification<ENTITY, FILTER extends SpaceEntityFilter> extends AbstractSpecification<ENTITY> {

    public final Specification<ENTITY> getSpecification(FILTER filter) {
        return getCommonSpecification(filter)
            .and(getEntitySpecification(filter));
    }

    private Specification<ENTITY> getCommonSpecification(FILTER filter) {
        return Specification.where(nameLike(filter.getName()));
    }

    private Specification<ENTITY> nameLike(String name) {
        return !Utils.isNullOrBlank(name)
            ? like(SpaceEntity_.NAME, name)
            : null;
    }

    abstract Specification<ENTITY> getEntitySpecification(FILTER filter);
}
