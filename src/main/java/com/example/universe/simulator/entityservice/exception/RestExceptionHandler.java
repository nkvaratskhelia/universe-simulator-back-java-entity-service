package com.example.universe.simulator.entityservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    private ResponseEntity<RestErrorResponse> handleHttpRequestMethodNotSupportedException() {
        return buildErrorResponse(ErrorCodeType.WRONG_HTTP_METHOD);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    private ResponseEntity<RestErrorResponse> handleBadRequestException() {
        return buildErrorResponse(ErrorCodeType.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<RestErrorResponse> handleUnknownException() {
        return buildErrorResponse(ErrorCodeType.SERVER_ERROR);
    }
}
