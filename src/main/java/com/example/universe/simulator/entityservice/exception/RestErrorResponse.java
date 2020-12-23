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

    public RestErrorResponse(ErrorCodeType errorCode) {
        this.error = errorCode;
        timestamp = Instant.now();
        status = errorCode.getHttpStatus().value();
    }
}
