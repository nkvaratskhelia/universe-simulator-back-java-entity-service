package com.example.universe.simulator.entityservice.exception;

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

@Slf4j
@RestControllerAdvice
class RestExceptionHandler {

    private ResponseEntity<RestErrorResponse> buildErrorResponse(ErrorCodeType errorCode, Exception exception) {
        log.error("", exception);
        RestErrorResponse response = new RestErrorResponse(errorCode);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(AppException.class)
    private ResponseEntity<RestErrorResponse> handleAppException(AppException exception) {
        return buildErrorResponse(exception.getErrorCode(), exception);
    }

    //thrown when trying to delete non-existent entity
    @ExceptionHandler(EmptyResultDataAccessException.class)
    private ResponseEntity<RestErrorResponse> handleEmptyResultDataAccessException(EmptyResultDataAccessException exception) {
        log.error("", exception);
        return buildErrorResponse(ErrorCodeType.NOT_FOUND_ENTITY, exception);
    }

    //thrown when content type is not specified or is something other than json
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    private ResponseEntity<RestErrorResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {
        log.error("", exception);
        return buildErrorResponse(ErrorCodeType.INVALID_CONTENT_TYPE, exception);
    }

    //thrown when request body is missing or cannot be deserialized
    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<RestErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.error("", exception);
        return buildErrorResponse(ErrorCodeType.INVALID_REQUEST_BODY, exception);
    }

    //thrown when wrong http method is used
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    private ResponseEntity<RestErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        log.error("", exception);
        return buildErrorResponse(ErrorCodeType.INVALID_HTTP_METHOD, exception);
    }

    //thrown when request parameter cannot be processed
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private ResponseEntity<RestErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        log.error("", exception);
        return buildErrorResponse(ErrorCodeType.INVALID_REQUEST_PARAMETER, exception);
    }

    //thrown when request entity version does not match db version
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    private ResponseEntity<RestErrorResponse> handleObjectOptimisticLockingFailureException(ObjectOptimisticLockingFailureException exception) {
        log.error("", exception);
        return buildErrorResponse(ErrorCodeType.ENTITY_MODIFIED, exception);
    }

    //thrown when passing invalid sort parameter to paged requests
    @ExceptionHandler(PropertyReferenceException.class)
    private ResponseEntity<RestErrorResponse> handlePropertyReferenceException(PropertyReferenceException exception) {
        log.error("", exception);
        return buildErrorResponse(ErrorCodeType.INVALID_SORT_PARAMETER, exception);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<RestErrorResponse> handleUnknownException(Exception exception) {
        log.error("", exception);
        return buildErrorResponse(ErrorCodeType.SERVER_ERROR, exception);
    }
}
