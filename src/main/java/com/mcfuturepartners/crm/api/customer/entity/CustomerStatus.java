package com.mcfuturepartners.crm.api.customer.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CustomerStatus {
    NEWLY_ESTABILSHED("신규 고객"),
    FAILED("영업 실패 고객"),
    ON_HOLD("영업 보류 고객"),
    IN_PROGRESS("영업 진행 중 고객"),
    SUCCESSFUL("영업 성공 고객");
    private final String msg;
}
