package com.example.universe.simulator.entityservice.specifications;

import com.example.universe.simulator.entityservice.entities.SpaceEntity;
import com.example.universe.simulator.entityservice.entities.SpaceEntity_;
import com.example.universe.simulator.entityservice.filters.SpaceEntityFilter;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

abstract class SpaceEntitySpecificationBuilder<E extends SpaceEntity, F extends SpaceEntityFilter> {

    public final Specification<@NonNull E> build(F filter) {
        return buildCommonSpecification(filter)
            .and(buildEntitySpecification(filter));
    }

    private Specification<@NonNull E> buildCommonSpecification(F filter) {
        return Specification.where(nameLike(filter.getName()));
    }

    private Specification<@NonNull E> nameLike(String name) {
        return StringUtils.hasText(name)
               ? SpecificationUtils.like(SpaceEntity_.NAME, name)
               : Specification.unrestricted();
    }

    abstract Specification<@NonNull E> buildEntitySpecification(F filter);
}
