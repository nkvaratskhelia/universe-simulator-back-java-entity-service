package com.example.universe.simulator.entityservice.common.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.PageImpl;

import java.util.List;

// Mock implementation of org.springframework.data.domain.Page for jackson deserialization.
public class JsonPage<T> extends PageImpl<@NonNull T> {

    public JsonPage(@JsonProperty("content") List<T> content) {
        super(content);
    }
}
