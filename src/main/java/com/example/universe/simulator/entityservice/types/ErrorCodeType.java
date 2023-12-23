package com.example.universe.simulator.entityservice.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCodeType {

    ENTITY_MODIFIED(HttpStatus.CONFLICT),
    EXISTS_NAME(HttpStatus.CONFLICT),
    IN_USE(HttpStatus.CONFLICT),
    INVALID_SORT_PROPERTY(HttpStatus.BAD_REQUEST),
    MISSING_PARAMETER_GALAXY_ID(HttpStatus.BAD_REQUEST),
    MISSING_PARAMETER_ID(HttpStatus.BAD_REQUEST),
    MISSING_PARAMETER_NAME(HttpStatus.BAD_REQUEST),
    MISSING_PARAMETER_PLANET_ID(HttpStatus.BAD_REQUEST),
    MISSING_PARAMETER_STAR_ID(HttpStatus.BAD_REQUEST),
    MISSING_PARAMETER_VERSION(HttpStatus.BAD_REQUEST),
    NOT_FOUND_ENTITY(HttpStatus.NOT_FOUND),
    NOT_FOUND_GALAXY(HttpStatus.NOT_FOUND),
    NOT_FOUND_PLANET(HttpStatus.NOT_FOUND),
    NOT_FOUND_STAR(HttpStatus.NOT_FOUND),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

    private final HttpStatus httpStatus;
}
