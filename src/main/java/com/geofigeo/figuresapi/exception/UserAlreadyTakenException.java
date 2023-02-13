package com.geofigeo.figuresapi.exception;

public class UserAlreadyTakenException extends RuntimeException {
    public UserAlreadyTakenException(String message) {
        super(message);
    }
}
