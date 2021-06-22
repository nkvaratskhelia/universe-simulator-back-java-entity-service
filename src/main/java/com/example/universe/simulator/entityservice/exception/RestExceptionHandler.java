package com.example.universe.simulator.entityservice.exception;

import com.example.universe.simulator.entityservice.types.ErrorCodeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(AppException.class)
    private ResponseEntity<ErrorResponse> handleAppException(AppException exception) {
        return buildErrorResponse(exception.getErrorCode(), exception);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(ErrorCodeType errorCode, Exception exception) {
        log.error("", exception);

        ErrorResponse errorResponse = ErrorResponse.builder()
            .error(errorCode)
            .time(Instant.now())
            .build();
        return ResponseEntity
            .status(errorCode.getHttpStatus())
            .body(errorResponse);
    }

    // thrown when trying to delete non-existent entity
    @ExceptionHandler(EmptyResultDataAccessException.class)
    private ResponseEntity<ErrorResponse> handleEmptyResultDataAccessException(EmptyResultDataAccessException exception) {
        return buildErrorResponse(ErrorCodeType.NOT_FOUND_ENTITY, exception);
    }

    // thrown when content type is not specified or is something other than json
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    private ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {
        return buildErrorResponse(ErrorCodeType.INVALID_CONTENT_TYPE, exception);
    }

    // thrown when request body is missing or cannot be deserialized
    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        return buildErrorResponse(ErrorCodeType.INVALID_REQUEST_BODY, exception);
    }

    // thrown when wrong http method is used
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    private ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        return buildErrorResponse(ErrorCodeType.INVALID_HTTP_METHOD, exception);
    }

    // thrown when request parameter cannot be processed
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        return buildErrorResponse(ErrorCodeType.INVALID_REQUEST_PARAMETER, exception);
    }

    // thrown when request entity version does not match db version
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    private ResponseEntity<ErrorResponse> handleObjectOptimisticLockingFailureException(ObjectOptimisticLockingFailureException exception) {
        return buildErrorResponse(ErrorCodeType.ENTITY_MODIFIED, exception);
    }

    // thrown when passing invalid sort parameter to paged requests
    @ExceptionHandler(PropertyReferenceException.class)
    private ResponseEntity<ErrorResponse> handlePropertyReferenceException(PropertyReferenceException exception) {
        return buildErrorResponse(ErrorCodeType.INVALID_SORT_PARAMETER, exception);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ErrorResponse> handleUnknownException(Exception exception) {
        return buildErrorResponse(ErrorCodeType.SERVER_ERROR, exception);
    }
}
