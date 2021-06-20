package com.example.universe.simulator.entityservice.exception;

import com.example.universe.simulator.entityservice.types.ErrorCodeType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter @Setter
@Builder
public class ErrorResponse {

    private ErrorCodeType error;
    private Instant time;
}
