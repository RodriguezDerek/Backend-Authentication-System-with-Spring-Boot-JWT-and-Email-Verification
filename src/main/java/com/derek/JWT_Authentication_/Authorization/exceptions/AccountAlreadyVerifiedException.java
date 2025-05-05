package com.derek.JWT_Authentication_.Authorization.exceptions;

public class AccountAlreadyVerifiedException extends RuntimeException {
    public AccountAlreadyVerifiedException(String message) {
        super(message);
    }
}
