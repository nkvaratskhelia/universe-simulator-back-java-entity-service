package com.example.universe.simulator.entityservice.exception;

import com.example.universe.simulator.common.dtos.ErrorDto;
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

@RestControllerAdvice
@Slf4j
class RestExceptionHandler {

    @ExceptionHandler(AppException.class)
    private ResponseEntity<ErrorDto> handleAppException(AppException exception) {
        return buildResponse(exception.getErrorCode(), exception);
    }

    private ResponseEntity<ErrorDto> buildResponse(ErrorCodeType errorCode, Exception exception) {
        log.error("", exception);

        return ResponseEntity
            .status(errorCode.getHttpStatus())
            .body(new ErrorDto(errorCode.toString(), Instant.now()));
    }

    // thrown when trying to delete non-existent entity
    @ExceptionHandler(EmptyResultDataAccessException.class)
    private ResponseEntity<ErrorDto> handleEmptyResultDataAccessException(EmptyResultDataAccessException exception) {
        return buildResponse(ErrorCodeType.NOT_FOUND_ENTITY, exception);
    }

    // thrown when content type is not specified or is something other than json
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    private ResponseEntity<ErrorDto> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {
        return buildResponse(ErrorCodeType.INVALID_CONTENT_TYPE, exception);
    }

    // thrown when request body is missing or cannot be deserialized
    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<ErrorDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        return buildResponse(ErrorCodeType.INVALID_REQUEST_BODY, exception);
    }

    // thrown when wrong http method is used
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    private ResponseEntity<ErrorDto> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        return buildResponse(ErrorCodeType.INVALID_HTTP_METHOD, exception);
    }

    // thrown when request parameter cannot be processed
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private ResponseEntity<ErrorDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        return buildResponse(ErrorCodeType.INVALID_REQUEST_PARAMETER, exception);
    }

    // thrown when request entity version does not match db version
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    private ResponseEntity<ErrorDto> handleObjectOptimisticLockingFailureException(ObjectOptimisticLockingFailureException exception) {
        return buildResponse(ErrorCodeType.ENTITY_MODIFIED, exception);
    }

    // thrown when passing invalid sort property to paged requests
    @ExceptionHandler(PropertyReferenceException.class)
    private ResponseEntity<ErrorDto> handlePropertyReferenceException(PropertyReferenceException exception) {
        return buildResponse(ErrorCodeType.INVALID_SORT_PROPERTY, exception);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ErrorDto> handleUnknownException(Exception exception) {
        return buildResponse(ErrorCodeType.SERVER_ERROR, exception);
    }
}
