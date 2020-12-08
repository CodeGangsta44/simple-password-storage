package com.yurwar.simplepasswordstorage.utils;

public class BlockedIpException extends RuntimeException {
    private String ip;

    public BlockedIpException(String ip) {
        this.ip = ip;
    }

    public BlockedIpException(String message, String ip) {
        super(message);
        this.ip = ip;
    }

    public BlockedIpException(String message, Throwable cause, String ip) {
        super(message, cause);
        this.ip = ip;
    }

    public BlockedIpException(Throwable cause, String ip) {
        super(cause);
        this.ip = ip;
    }

    public BlockedIpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String ip) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.ip = ip;
    }

    @Override
    public String getMessage() {
        return "Current ip %s is blocked. Try again later".formatted(ip);
    }

    @Override
    public String getLocalizedMessage() {
        return "Current ip %s is blocked. Try again later".formatted(ip);
    }
}
