package com.sb.main.exception;

//@ResponseStatus(value = HttpStatus.NOT_FOUND) //404
public class TaskNotFoundException extends RuntimeException
{
    private String message;

    public TaskNotFoundException(String message)
    {
        super(message);
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }
}
