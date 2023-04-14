package ru.zalimannard.dripchip.exception.advice;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.zalimannard.dripchip.exception.response.ExceptionMessage;
import ru.zalimannard.dripchip.exception.response.ExceptionResponse;
import ru.zalimannard.dripchip.exception.response.HttpCodes;

import java.util.List;

@ControllerAdvice
public class ValidationAdvice {

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse onConstraintValidationException(
            ConstraintViolationException e
    ) {
        List<ExceptionMessage> errors = e.getConstraintViolations().stream().map(
                violation -> ExceptionMessage.builder()
                        .details(violation.getMessage())
                        .field(violation.getPropertyPath().toString()).build()
        ).toList();
        return ExceptionResponse.builder()
                .httpCode(HttpCodes.BAD_REQUEST.getCode())
                .message("Значение не удовлетворяют ограничениям")
                .code("v-001")
                .errors(errors)
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponse onMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        return ExceptionResponse.builder()
                .httpCode(HttpCodes.BAD_REQUEST.getCode())
                .code("v-002")
                .message("Некорректные аргументы")
                .errors(e.getBindingResult().getFieldErrors().stream()
                        .map(error -> ExceptionMessage.builder()
                                .details(error.getDefaultMessage()).build()
                        ).toList()
                )
                .build();
    }

}
