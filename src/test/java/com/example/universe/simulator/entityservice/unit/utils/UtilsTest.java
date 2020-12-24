package com.example.universe.simulator.entityservice.unit.utils;

import com.example.universe.simulator.entityservice.utils.Utils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UtilsTest {

    @Test
    void testIsNullOrBlank() {
        //when
        boolean result = Utils.isNullOrBlank(null);
        //then
        assertTrue(result);

        //given
        String str = "";
        //when
        result = Utils.isNullOrBlank(str);
        //then
        assertTrue(result);

        //given
        str = " ";
        //when
        result = Utils.isNullOrBlank(str);
        //then
        assertTrue(result);

        //given
        str = "str";
        //when
        result = Utils.isNullOrBlank(str);
        //then
        assertFalse(result);
    }
}
