package com.derek.JWT_Authentication_.Authorization.exceptions;

public class InvalidVerificationCodeException extends RuntimeException {
    public InvalidVerificationCodeException(String message) {
        super(message);
    }
}
