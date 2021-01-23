package com.example.universe.simulator.entityservice.common.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * Mock implementation of org.springframework.data.domain.Page for jackson deserialization.
 */
public class JsonPage<T> extends PageImpl<T> {

    public JsonPage(@JsonProperty("content") List<T> content,
                    @JsonProperty("number") int number,
                    @JsonProperty("size") int size,
                    @JsonProperty("totalElements") long totalElements,
                    @JsonProperty("pageable") JsonNode pageable) {
        super(content, PageRequest.of(number, size), totalElements);
    }
}
