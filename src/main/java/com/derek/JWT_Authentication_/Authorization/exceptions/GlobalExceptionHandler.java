package com.derek.JWT_Authentication_.Authorization.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUsernameNotFoundException(UsernameNotFoundException exception){
        LocalDateTime timestamp = LocalDateTime.now();
        return new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND.value(), timestamp);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationError(MethodArgumentNotValidException exception){
        String errorMessage = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        LocalDateTime timestamp = LocalDateTime.now();
        return new ErrorResponse(errorMessage, HttpStatus.BAD_REQUEST.value(), timestamp);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUsernameNotFoundException(UsernameAlreadyExistsException exception){
        LocalDateTime timestamp = LocalDateTime.now();
        return new ErrorResponse(exception.getMessage(), HttpStatus.CONFLICT.value(), timestamp);
    }

    @ExceptionHandler(InvalidLoginException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUsernameNotFoundException(InvalidLoginException exception){
        LocalDateTime timestamp = LocalDateTime.now();
        return new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST.value(), timestamp);
    }

    @ExceptionHandler(AccountNotVerifiedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUsernameNotFoundException(AccountNotVerifiedException exception){
        LocalDateTime timestamp = LocalDateTime.now();
        return new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST.value(), timestamp);
    }

    @ExceptionHandler(VerificationCodeExpiredException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleVerificationCodeExpiredException(VerificationCodeExpiredException exception){
        LocalDateTime timestamp = LocalDateTime.now();
        return new ErrorResponse(exception.getMessage(), HttpStatus.CONFLICT.value(), timestamp);
    }

    @ExceptionHandler(InvalidVerificationCodeException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidVerificationCodeException(InvalidVerificationCodeException exception){
        LocalDateTime timestamp = LocalDateTime.now();
        return new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST.value(), timestamp);
    }

    @ExceptionHandler(AccountAlreadyVerifiedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAccountAlreadyVerifiedException(AccountAlreadyVerifiedException exception){
        LocalDateTime timestamp = LocalDateTime.now();
        return new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST.value(), timestamp);
    }
}
