package com.cloudcommander.vendor.module.validators.exceptions;

/**
 * Created by Adrian Tello on 10/06/2017.
 */
public class ValidationFailedException extends Exception{
    public ValidationFailedException() {
    }

    public ValidationFailedException(String message) {
        super(message);
    }

    public ValidationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationFailedException(Throwable cause) {
        super(cause);
    }

    public ValidationFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
