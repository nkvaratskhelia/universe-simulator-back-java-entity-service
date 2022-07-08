package com.example.universe.simulator.entityservice.unit.specifications;

import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.filters.GalaxyFilter;
import com.example.universe.simulator.entityservice.specifications.GalaxySpecificationBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import static org.assertj.core.api.Assertions.assertThat;

// Common space entity specification building is tested using GalaxySpecificationBuilder.
@ExtendWith(MockitoExtension.class)
class SpaceEntitySpecificationBuilderTest {

    @InjectMocks
    private GalaxySpecificationBuilder specificationBuilder;

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void testBuild_blankName(String name) {
        // given
        GalaxyFilter filter = GalaxyFilter.builder().name(name).build();
        // when
        Specification<Galaxy> result = specificationBuilder.build(filter);
        // then
        assertThat(result).isNotNull();
    }

    @Test
    void testBuild() {
        // given
        GalaxyFilter filter = TestUtils.buildGalaxyFilter();
        // when
        Specification<Galaxy> result = specificationBuilder.build(filter);
        // then
        assertThat(result).isNotNull();
    }
}
