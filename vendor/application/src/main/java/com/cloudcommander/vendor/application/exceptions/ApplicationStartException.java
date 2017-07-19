package com.cloudcommander.vendor.application.exceptions;

/**
 * Created by Adrian Tello on 09/06/2017.
 */
public class ApplicationStartException extends Exception{
    public ApplicationStartException() {
    }

    public ApplicationStartException(String message) {
        super(message);
    }

    public ApplicationStartException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationStartException(Throwable cause) {
        super(cause);
    }

    public ApplicationStartException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
