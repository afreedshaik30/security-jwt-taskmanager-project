package com.sb.main.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(value = HttpStatus.BAD_REQUEST) //400
public class ApiException extends RuntimeException
{
    private String message;

    public ApiException(String message)
    {
        super(message);
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
