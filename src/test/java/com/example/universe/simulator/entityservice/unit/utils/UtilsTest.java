package com.example.universe.simulator.entityservice.unit.utils;

import com.example.universe.simulator.entityservice.utils.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class UtilsTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void testIsNullOrBlank_invalidString(String argument) {
        //when
        boolean result = Utils.isNullOrBlank(argument);
        //then
        assertThat(result).isTrue();
    }

    @Test
    void testIsNullOrBlank_validString() {
        //given
        String str = "str";
        //when
        boolean result = Utils.isNullOrBlank(str);
        //then
        assertThat(result).isFalse();
    }
}
