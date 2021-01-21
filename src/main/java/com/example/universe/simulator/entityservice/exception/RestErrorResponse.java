package com.example.universe.simulator.entityservice.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter @Setter
@NoArgsConstructor
public class RestErrorResponse {

    private Instant timestamp;
    private int status;
    private ErrorCodeType error;
    private String message;

    RestErrorResponse(ErrorCodeType errorCode, String message) {
        this.error = errorCode;
        timestamp = Instant.now();
        status = errorCode.getHttpStatus().value();
        this.message = message;
    }
}
