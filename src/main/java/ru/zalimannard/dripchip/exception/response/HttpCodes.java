package ru.zalimannard.dripchip.exception.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum HttpCodes {

    BAD_REQUEST("400 - BAD REQUEST"),
    UNAUTHORIZED("401 - UNAUTHORIZED"),
    FORBIDDEN("403 - FORBIDDEN"),
    NOT_FOUND("404 - NOT FOUND"),
    CONFLICT("409 - CONFLICT");

    @Getter
    private final String code;

}