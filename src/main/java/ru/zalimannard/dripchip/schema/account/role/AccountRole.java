package ru.zalimannard.dripchip.schema.account.role;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum AccountRole {
    ADMIN("A"),
    CHIPPER("C"),
    USER("U");

    @Getter
    private final String code;
}
