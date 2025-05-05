package com.derek.JWT_Authentication_.Authorization.exceptions;

public class AccountNotVerifiedException extends RuntimeException {
    public AccountNotVerifiedException(String message) {
        super(message);
    }
}
