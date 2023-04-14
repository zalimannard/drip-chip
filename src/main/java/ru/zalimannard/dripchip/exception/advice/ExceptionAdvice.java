package ru.zalimannard.dripchip.exception.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.zalimannard.dripchip.exception.BadRequestException;
import ru.zalimannard.dripchip.exception.ConflictException;
import ru.zalimannard.dripchip.exception.NotFoundException;
import ru.zalimannard.dripchip.exception.response.ExceptionResponse;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> badRequestException(BadRequestException exception) {
        ExceptionResponse responseBody = ExceptionResponse.builder()
                .httpCode(exception.getHttpCode().getCode())
                .code(exception.getCode())
                .message(exception.getMessage())
                .errors(exception.getErrors())
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseBody);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> notFoundException(NotFoundException exception) {
        ExceptionResponse responseBody = ExceptionResponse.builder()
                .httpCode(exception.getHttpCode().getCode())
                .code(exception.getCode())
                .message(exception.getMessage())
                .errors(exception.getErrors())
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseBody);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ExceptionResponse> conflictException(ConflictException exception) {
        ExceptionResponse responseBody = ExceptionResponse.builder()
                .httpCode(exception.getHttpCode().getCode())
                .code(exception.getCode())
                .message(exception.getMessage())
                .errors(exception.getErrors())
                .build();
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(responseBody);
    }

}
