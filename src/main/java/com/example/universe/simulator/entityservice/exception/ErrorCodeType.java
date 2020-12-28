package com.example.universe.simulator.entityservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCodeType {

    ENTITY_MODIFIED(HttpStatus.CONFLICT),
    EXISTS_NAME(HttpStatus.CONFLICT),
    IN_USE(HttpStatus.CONFLICT),
    INVALID_CONTENT_TYPE(HttpStatus.BAD_REQUEST),
    INVALID_HTTP_METHOD(HttpStatus.BAD_REQUEST),
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST),
    INVALID_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST),
    MISSING_PARAMETER_GALAXY(HttpStatus.BAD_REQUEST),
    MISSING_PARAMETER_GALAXY_ID(HttpStatus.BAD_REQUEST),
    MISSING_PARAMETER_ID(HttpStatus.BAD_REQUEST),
    MISSING_PARAMETER_NAME(HttpStatus.BAD_REQUEST),
    MISSING_PARAMETER_VERSION(HttpStatus.BAD_REQUEST),
    NOT_FOUND_ENTITY(HttpStatus.NOT_FOUND),
    NOT_FOUND_GALAXY(HttpStatus.NOT_FOUND),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

    private final HttpStatus httpStatus;
}
