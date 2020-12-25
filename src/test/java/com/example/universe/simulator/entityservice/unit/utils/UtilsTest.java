package com.example.universe.simulator.entityservice.unit.utils;

import com.example.universe.simulator.entityservice.utils.Utils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UtilsTest {

    @Test
    void testIsNullOrBlank_nullString() {
        //when
        boolean result = Utils.isNullOrBlank(null);
        //then
        assertThat(result).isTrue();
    }

    @Test
    void testIsNullOrBlank_emptyString() {
        //given
        String str = "";
        //when
        boolean result = Utils.isNullOrBlank(str);
        //then
        assertThat(result).isTrue();
    }

    @Test
    void testIsNullOrBlank_blankString() {
        //given
        String str = " ";
        //when
        boolean result = Utils.isNullOrBlank(str);
        //then
        assertThat(result).isTrue();
    }

    @Test
    void testIsNullOrBlank_properString() {
        //given
        String str = "str";
        //when
        boolean result = Utils.isNullOrBlank(str);
        //then
        assertThat(result).isFalse();
    }
}
