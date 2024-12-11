package com.sb.main.exception;

import java.time.LocalDateTime;
import java.time.LocalDateTime;

public class ErrorResponseDetails
{
    private LocalDateTime timeStamp;
    private String errorStatusCode;
    private String message;
    private String uriPath;

    public ErrorResponseDetails(LocalDateTime timeStamp, String errorStatusCode, String message, String uriPath)
    {
        this.timeStamp = timeStamp;
        this.errorStatusCode = errorStatusCode;
        this.message = message;
        this.uriPath = uriPath;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getErrorStatusCode() {
        return errorStatusCode;
    }

    public void setErrorStatusCode(String errorStatusCode) {
        this.errorStatusCode = errorStatusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUriPath() {
        return uriPath;
    }

    public void setUriPath(String uriPath) {
        this.uriPath = uriPath;
    }
}
