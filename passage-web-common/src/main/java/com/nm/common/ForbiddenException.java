package com.nm.common;

public class ForbiddenException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 2L;

    public ForbiddenException() {

    }

    public ForbiddenException(String message) {
        super(message);
    }

}
