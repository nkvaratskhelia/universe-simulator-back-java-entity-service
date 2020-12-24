package com.example.universe.simulator.entityservice.exception;

import org.springframework.dao.DataIntegrityViolationException;
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

    //thrown when some db unique field constraint is violated
    @ExceptionHandler(DataIntegrityViolationException.class)
    private ResponseEntity<RestErrorResponse> handleDataIntegrityViolationException() {
        return buildErrorResponse(ErrorCodeType.ENTITY_EXISTS);
    }

    //thrown when content type is not specified or is something other than json
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    private ResponseEntity<RestErrorResponse> handleHttpMediaTypeNotSupportedException() {
        return buildErrorResponse(ErrorCodeType.INVALID_CONTENT_TYPE);
    }

    //thrown when request body is missing or cannot be deserialized
    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<RestErrorResponse> handleHttpMessageNotReadableException() {
        return buildErrorResponse(ErrorCodeType.INVALID_REQUEST_BODY);
    }

    //thrown when wrong http method is used
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    private ResponseEntity<RestErrorResponse> handleHttpRequestMethodNotSupportedException() {
        return buildErrorResponse(ErrorCodeType.INVALID_HTTP_METHOD);
    }

    //thrown when request parameter cannot be processed
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private ResponseEntity<RestErrorResponse> handleMethodArgumentTypeMismatchException() {
        return buildErrorResponse(ErrorCodeType.INVALID_REQUEST_PARAMETER);
    }

    //thrown when request entity version does not match db version
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    private ResponseEntity<RestErrorResponse> handleObjectOptimisticLockingFailureException() {
        return buildErrorResponse(ErrorCodeType.ENTITY_MODIFIED);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<RestErrorResponse> handleUnknownException() {
        return buildErrorResponse(ErrorCodeType.SERVER_ERROR);
    }
}
