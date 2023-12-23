package com.example.universe.simulator.entityservice.exception;

import com.example.universe.simulator.entityservice.types.ErrorCodeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.ProblemDetail;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    public static final String TIMESTAMP_PROPERTY = "timestamp";

    @ExceptionHandler(AppException.class)
    private ProblemDetail handleAppException(AppException exception) {
        return buildResponse(exception.getErrorCode(), exception);
    }

    private ProblemDetail buildResponse(ErrorCodeType errorCode, Exception exception) {
        log.error("", exception);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(errorCode.getHttpStatus(), errorCode.toString());
        problemDetail.setProperty(TIMESTAMP_PROPERTY, Instant.now());

        return problemDetail;
    }

    // thrown when trying to delete non-existent entity
    @ExceptionHandler(EmptyResultDataAccessException.class)
    private ProblemDetail handleEmptyResultDataAccessException(EmptyResultDataAccessException exception) {
        return buildResponse(ErrorCodeType.NOT_FOUND_ENTITY, exception);
    }

    // thrown when request entity version does not match db version
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    private ProblemDetail handleObjectOptimisticLockingFailureException(ObjectOptimisticLockingFailureException exception) {
        return buildResponse(ErrorCodeType.ENTITY_MODIFIED, exception);
    }

    // thrown when passing invalid sort property to paged requests
    @ExceptionHandler(PropertyReferenceException.class)
    private ProblemDetail handlePropertyReferenceException(PropertyReferenceException exception) {
        return buildResponse(ErrorCodeType.INVALID_SORT_PROPERTY, exception);
    }

    @ExceptionHandler(Exception.class)
    private ProblemDetail handleUnknownException(Exception exception) {
        return buildResponse(ErrorCodeType.SERVER_ERROR, exception);
    }
}
