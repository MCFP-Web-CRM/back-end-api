package com.mcfuturepartners.crm.api.entity;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@Table(name = "authority")
@RequiredArgsConstructor
public enum Authority implements GrantedAuthority {
    ADMIN("ROLE_ADMIN","관리자권한"),
    USER("ROLE_USER","사용자권한"),
    UNKNOWN_USER("ROLE_UNKNOWN_USER","알수없는 사용자");
    private final String code;
    private final String description;

    @Override
    public String getAuthority() { return name(); }
}
