package com.derek.JWT_Authentication_.Authorization.exceptions;

public class VerificationCodeExpiredException extends RuntimeException {
    public VerificationCodeExpiredException(String message) {
        super(message);
    }
}
