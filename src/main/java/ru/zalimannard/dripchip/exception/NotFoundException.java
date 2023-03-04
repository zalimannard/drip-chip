package ru.zalimannard.dripchip.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException(String objectName, String fieldName, String value) {
        super(objectName + " with " + fieldName + "=" + value + " not found");
    }

}