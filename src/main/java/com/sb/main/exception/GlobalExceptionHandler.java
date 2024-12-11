package com.sb.main.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(UserNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
    public  ResponseEntity<ErrorResponseDetails> handleUserNotFoundException(UserNotFoundException ex, WebRequest webRequest)
    {
        ErrorResponseDetails errorResponse = new ErrorResponseDetails(
                LocalDateTime.now(),
                "USER_NOT_FOUND",
                ex.getMessage(),
                webRequest.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TaskNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponseDetails> handleTaskNotFoundException(TaskNotFoundException ex, WebRequest webRequest)
    {
        ErrorResponseDetails errorResponse = new ErrorResponseDetails(
                LocalDateTime.now(),
                "TASK_AND_USER_MISMATCH",
                ex.getMessage(),
                webRequest.getDescription(false)
        );
       return new ResponseEntity<>(errorResponse,HttpStatus.OK);
    }
    @ExceptionHandler(ApiException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDetails> handleApiException(ApiException ex, WebRequest webRequest)
    {
        ErrorResponseDetails errorResponse = new ErrorResponseDetails(
                LocalDateTime.now(),
                "TASK_NOT_FOUND",
                ex.getMessage(),
                webRequest.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse,HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponseDetails> genericException(Exception ex, WebRequest webRequest)
    {
      ErrorResponseDetails errorResponse = new ErrorResponseDetails(
              LocalDateTime.now(),
              "INTERNAL_SERVER_ERROR",
              ex.getMessage(),
              webRequest.getDescription(false)
      );
      return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
