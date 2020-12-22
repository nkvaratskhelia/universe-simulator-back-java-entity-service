package com.example.universe.simulator.entityservice.exception;

import lombok.Getter;

@Getter
public class AppException extends Exception {

    private final ErrorCodeType errorCode;

    public AppException(ErrorCodeType errorCode) {
        super(errorCode.toString());
        this.errorCode = errorCode;
    }
}
