package com.example.universe.simulator.entityservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(AppException.class)
    private ResponseEntity<RestErrorResponse> handleAppException(AppException exception) {
        return buildErrorResponse(exception.getErrorCode());
    }

    private ResponseEntity<RestErrorResponse> buildErrorResponse(ErrorCodeType errorCode) {
        RestErrorResponse response = new RestErrorResponse(errorCode);

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    private ResponseEntity<RestErrorResponse> handleHttpMediaTypeNotSupportedException() {
        return buildErrorResponse(ErrorCodeType.INVALID_CONTENT_TYPE);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<RestErrorResponse> handleHttpMessageNotReadableException() {
        return buildErrorResponse(ErrorCodeType.INVALID_REQUEST_BODY);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    private ResponseEntity<RestErrorResponse> handleHttpRequestMethodNotSupportedException() {
        return buildErrorResponse(ErrorCodeType.INVALID_HTTP_METHOD);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private ResponseEntity<RestErrorResponse> handleMethodArgumentTypeMismatchException() {
        return buildErrorResponse(ErrorCodeType.INVALID_REQUEST_PARAMETER);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    private ResponseEntity<RestErrorResponse> handleObjectOptimisticLockingFailureException() {
        return buildErrorResponse(ErrorCodeType.ENTITY_MODIFIED);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<RestErrorResponse> handleUnknownException() {
        return buildErrorResponse(ErrorCodeType.SERVER_ERROR);
    }
}
