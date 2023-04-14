package ru.zalimannard.dripchip.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.zalimannard.dripchip.exception.response.ExceptionMessage;
import ru.zalimannard.dripchip.exception.response.HttpCodes;

import java.util.List;

@AllArgsConstructor
@Getter
public abstract class BaseException extends RuntimeException {

    private final HttpCodes httpCode;

    private final String code;

    private final String message;

    private final List<ExceptionMessage> errors;

}
