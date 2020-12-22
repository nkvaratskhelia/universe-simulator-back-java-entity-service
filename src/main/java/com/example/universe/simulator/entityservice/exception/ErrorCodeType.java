package com.example.universe.simulator.entityservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCodeType {

    BAD_REQUEST(HttpStatus.BAD_REQUEST),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    WRONG_HTTP_METHOD(HttpStatus.METHOD_NOT_ALLOWED);

    private final HttpStatus httpStatus;
}
