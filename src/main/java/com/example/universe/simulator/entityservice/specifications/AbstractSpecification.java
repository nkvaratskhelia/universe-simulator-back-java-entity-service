package com.example.universe.simulator.entityservice.specifications;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
class AbstractSpecification {

    <T> Specification<T> like(String property, String value) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(
                criteriaBuilder.lower(root.get(property)),
                "%" + value.toLowerCase() + "%"
            );
    }
}
