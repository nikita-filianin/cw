package com.warehouse.springwarehouseweb.models.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    CUSTOMER,
    MANAGER;

    @Override
    public String getAuthority() {
        return name();
    }
}
