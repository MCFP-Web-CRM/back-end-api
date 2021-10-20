package com.mcfuturepartners.crm.api.exception;

public class CounselException extends RuntimeException{
    private static final long SerializableUID = 1L;

    public CounselException() { super(); }

    public CounselException(String msg) { super(msg); }
}
