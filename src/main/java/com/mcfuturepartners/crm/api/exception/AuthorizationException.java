package com.mcfuturepartners.crm.api.exception;

public class AuthorizationException extends RuntimeException{
    private static final long SerializableUID = 1L;

    public AuthorizationException() { super(); }

    public AuthorizationException(String msg) { super(msg); }
}
