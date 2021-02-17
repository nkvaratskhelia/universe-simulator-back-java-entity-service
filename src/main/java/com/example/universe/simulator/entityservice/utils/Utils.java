package com.example.universe.simulator.entityservice.utils;

import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class Utils {

    public boolean isNullOrBlank(String str) {
        return Objects.isNull(str) || str.isBlank();
    }
}
