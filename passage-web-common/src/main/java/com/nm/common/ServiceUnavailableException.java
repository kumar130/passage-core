package com.nm.common;

public class ServiceUnavailableException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 3L;

    public ServiceUnavailableException() {

    }

    public ServiceUnavailableException(String message) {
        super(message);
    }

}
