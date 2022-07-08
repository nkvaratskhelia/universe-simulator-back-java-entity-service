package com.example.universe.simulator.entityservice.unit.specifications;

import com.example.universe.simulator.entityservice.specifications.SpecificationUtils;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import static org.assertj.core.api.Assertions.assertThat;

class SpecificationUtilsTest {

    @Test
    void testLike() {
        // when
        Specification<?> result = SpecificationUtils.like("property", "value");
        // then
        assertThat(result).isNotNull();
    }
}
