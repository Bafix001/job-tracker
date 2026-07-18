package com.bafix.jobtrackerbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException() { super(); }
    public EmailAlreadyUsedException(String message) { super(message); }
    public EmailAlreadyUsedException(String message, Throwable cause) { super(message, cause); }
}
