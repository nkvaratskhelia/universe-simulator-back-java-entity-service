package com.example.universe.simulator.entityservice.specifications;

import org.springframework.data.jpa.domain.Specification;

abstract class AbstractSpecification<T> {

    Specification<T> like(String property, String value) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(property)),
                        "%" + value.toLowerCase() + "%"
                );
    }
}
