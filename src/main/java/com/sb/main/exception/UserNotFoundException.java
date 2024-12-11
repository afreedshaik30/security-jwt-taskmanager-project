package com.sb.main.exception;

//@ResponseStatus(value = HttpStatus.NOT_FOUND)  //404
public class UserNotFoundException extends RuntimeException
{
    private String message;

    public UserNotFoundException(String message)
    {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage()
    {
        return message;
    }
}
