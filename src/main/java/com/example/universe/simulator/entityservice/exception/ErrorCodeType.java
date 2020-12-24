package com.example.universe.simulator.entityservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCodeType {

    ENTITY_EXISTS(HttpStatus.CONFLICT),
    ENTITY_MODIFIED(HttpStatus.CONFLICT),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND),
    INVALID_CONTENT_TYPE(HttpStatus.BAD_REQUEST),
    INVALID_HTTP_METHOD(HttpStatus.BAD_REQUEST),
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST),
    INVALID_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST),
    MISSING_PARAMETER_ID(HttpStatus.BAD_REQUEST),
    MISSING_PARAMETER_NAME(HttpStatus.BAD_REQUEST),
    MISSING_PARAMETER_VERSION(HttpStatus.BAD_REQUEST),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

    private final HttpStatus httpStatus;
}
