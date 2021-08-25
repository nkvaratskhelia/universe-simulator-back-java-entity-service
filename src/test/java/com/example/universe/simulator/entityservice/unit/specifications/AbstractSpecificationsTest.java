package com.example.universe.simulator.entityservice.unit.specifications;

import com.example.universe.simulator.entityservice.specifications.AbstractSpecifications;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import static org.assertj.core.api.Assertions.assertThat;

class AbstractSpecificationsTest {

    @Test
    void testLike() {
        // when
        Specification<?> result = AbstractSpecifications.like("property", "value");
        // then
        assertThat(result).isNotNull();
    }
}
