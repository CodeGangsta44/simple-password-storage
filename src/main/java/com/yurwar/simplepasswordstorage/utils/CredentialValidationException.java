package com.yurwar.simplepasswordstorage.utils;

public class CredentialValidationException extends RuntimeException {
    public CredentialValidationException() {
    }

    public CredentialValidationException(String message) {
        super(message);
    }

    public CredentialValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CredentialValidationException(Throwable cause) {
        super(cause);
    }

    public CredentialValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
