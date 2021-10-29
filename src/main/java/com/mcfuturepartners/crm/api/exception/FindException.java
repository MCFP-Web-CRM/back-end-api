package com.mcfuturepartners.crm.api.exception;

public class FindException extends RuntimeException{
    private static final long SerializableUID = 1L;

    public FindException() { super(); }

    public FindException(String msg) { super(msg); }
}
