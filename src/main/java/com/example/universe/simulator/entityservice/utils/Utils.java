package com.example.universe.simulator.entityservice.utils;

import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class Utils {

    public boolean isNullOrBlank(String str) {
        // TODO
        // !org.springframework.util.StringUtils.hasText(str) is shorter
        return Objects.isNull(str) || str.isBlank();
    }
}
