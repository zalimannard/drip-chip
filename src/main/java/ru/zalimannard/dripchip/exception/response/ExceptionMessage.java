package ru.zalimannard.dripchip.exception.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ExceptionMessage {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String details;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String field;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String value;

}
