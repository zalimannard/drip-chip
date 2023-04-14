package ru.zalimannard.dripchip.exception.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ExceptionResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String httpCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String message;

    @JsonInclude
    private final List<ExceptionMessage> errors;

}