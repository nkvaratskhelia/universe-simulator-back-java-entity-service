package com.example.universe.simulator.entityservice.specifications;

import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class SpecificationUtils {

    public <T> Specification<@NonNull T> like(String property, String value) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(
                criteriaBuilder.lower(root.get(property)),
                "%" + value.toLowerCase() + "%"
            );
    }
}
