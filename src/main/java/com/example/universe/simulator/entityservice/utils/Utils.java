package com.example.universe.simulator.entityservice.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Utils {

    public static boolean isNullOrBlank(String str) {
        return Objects.isNull(str) || str.isBlank();
    }
}
