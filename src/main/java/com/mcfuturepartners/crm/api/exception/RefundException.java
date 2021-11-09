package com.mcfuturepartners.crm.api.exception;

public class RefundException extends RuntimeException{
    private static final long SerializableUID = 1L;

    public RefundException() { super(); }

    public RefundException(String msg) { super(msg); }
}
