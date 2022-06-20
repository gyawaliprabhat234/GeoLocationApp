package com.geolocation.exception;

public class ApiException extends RuntimeException{
    public ApiException(String message) {
        super(message);
    }
}
