package com.mcfuturepartners.crm.api.user.exception;

public class LoginException extends RuntimeException{
    private static final long SerializableUID = 1L;

    public LoginException() { super(ErrorCode.LOGIN_FAILED.getMsg()); }

    public LoginException(String msg) { super(msg); }
}
