package com.mcfuturepartners.crm.api.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    BAD_REQUEST(400, "AUTH_001", "BAD_REQUEST"),
    AUTHENTICATION_FAILED(401, "AUTH_002", "AUTHENTICATION_FAILED"),
    LOGIN_FAILED(401, "AUTH_003", "LOGIN_FAILED"),
    ACCESS_DENIED(401, "AUTH_004", "ACCESS_DENIED"),
    USER_ALREADY_EXISTS(401, "AUTH_005", "USER_ALREADY_EXISTS"),
    UNAUTHORIZED(403, "AUTH_006", "UNAUTHORIZED"),
    TOKEN_GENERATION_FAILED(500, "AUTH_007", "TOKEN_GENERATION_FAILED");

    private final int status;
    private final String code;
    private final String msg;
}
