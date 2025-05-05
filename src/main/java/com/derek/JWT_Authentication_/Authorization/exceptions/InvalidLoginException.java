package com.derek.JWT_Authentication_.Authorization.exceptions;

public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException(String message) {
        super(message);
    }
}
