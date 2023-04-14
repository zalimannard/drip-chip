package ru.zalimannard.dripchip.exception;

import ru.zalimannard.dripchip.exception.response.ExceptionMessage;
import ru.zalimannard.dripchip.exception.response.HttpCodes;

import java.util.List;

public class ConflictException extends BaseException {

    public ConflictException(String code, String field, String value) {
        super(HttpCodes.CONFLICT,
                code,
                "Конфликт при работе с объектом",
                List.of(ExceptionMessage.builder()
                        .field(field)
                        .value(value).build()));
    }

}