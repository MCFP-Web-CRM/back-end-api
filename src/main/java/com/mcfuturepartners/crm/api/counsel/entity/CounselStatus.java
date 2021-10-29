package com.mcfuturepartners.crm.api.counsel.entity;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum CounselStatus {
    REFUSAL("거절"),
    HIGH_POSSIBILITY("가망(상)"),
    MIDDLE_POSSIBILITY("가망(중)"),
    LOW_POSSIBILITY("가망(하)"),
    ABSENCE("부재"),
    AGREEMENT("계약"),
    LONG_TERM_ABSENCE("장기부재"),
    CALL_COMPLETE("통화완료"),
    CALL_RESERVATION("통화예약");

    private final String status;
}
