package com.example.sbchainssioicdoauth2.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public final class MyResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MyResourceNotFoundException() {
        super();
    }

    public MyResourceNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MyResourceNotFoundException(final String message) {
        super(message);
    }

    public MyResourceNotFoundException(final Throwable cause) {
        super(cause);
    }

}