package com.example.universe.simulator.entityservice.utils;

import java.util.Objects;

public final class Utils {

    private Utils() {}

    public static boolean isNullOrBlank(String str) {
        return Objects.isNull(str) || str.isBlank();
    }
}
