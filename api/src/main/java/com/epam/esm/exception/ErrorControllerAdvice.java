package com.epam.esm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ErrorControllerAdvice {
    /**
     * Handles enum request parameters(url) conversion exception.
     */
    @ResponseBody
    @ExceptionHandler
    public ResponseEntity<ExceptionDto> handle(MethodArgumentTypeMismatchException ex) {
        String parameterName = ex.getName();
        String enumValues = Arrays.stream(ex.getRequiredType().getEnumConstants())
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        String message = String.format("Parameter '%s' must be either null or one of the following values: %s",
                parameterName, enumValues);
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setErrorMessage(message);
        exceptionDto.setErrorCode(400);
        exceptionDto.setTimestamp(LocalDateTime.now());
        return ResponseEntity.badRequest().body(exceptionDto);
    }

    /**
     * Handles errors from @Valid
     */
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<ExceptionDto>> handle(MethodArgumentNotValidException ex) {
        List<ExceptionDto> exceptionDtoList = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            String fieldName = ((FieldError) error).getField();
            ExceptionDto exceptionDto = new ExceptionDto();
            exceptionDto.setErrorMessage(String.format("%s - %s", fieldName, errorMessage));
            exceptionDto.setErrorCode(400);
            exceptionDto.setTimestamp(LocalDateTime.now());
            exceptionDtoList.add(exceptionDto);
        });
        return ResponseEntity.badRequest().body(exceptionDtoList);
    }

    /**
     * Handle errors from @Validated
     */
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<ExceptionDto>> handle(ConstraintViolationException ex) {
        List<ExceptionDto> exceptionDtoList = new ArrayList<>();
        ex.getConstraintViolations()
                .forEach(e -> {
                    String className = e.getRootBeanClass().getSimpleName();
                    String property = e.getPropertyPath().toString();
                    String message = e.getMessage();
                    String invalidValue = e.getInvalidValue().toString();
                    String fullMessage = String.format("%s.%s value '%s' %s", className, property, invalidValue, message);
                    ExceptionDto exceptionDto = new ExceptionDto();
                    exceptionDto.setErrorMessage(fullMessage);
                    exceptionDto.setErrorCode(400);
                    exceptionDto.setTimestamp(LocalDateTime.now());
                    exceptionDtoList.add(exceptionDto);
                });
        return ResponseEntity.badRequest().body(exceptionDtoList);
    }

    /**
     * Handle custom exception ResourceNotFoundException
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ExceptionDto> handleException(ResourceNotFoundException ex) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setErrorMessage(ex.getMessage());
        exceptionDto.setErrorCode(40401);
        exceptionDto.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(NOT_FOUND).body(exceptionDto);
    }

    /**
     * Run if url is not supported
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    ResponseEntity<ExceptionDto> handleNotFoundMapping(NoHandlerFoundException ex) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setErrorMessage(ex.getMessage());
        exceptionDto.setErrorCode(NOT_FOUND.value());
        exceptionDto.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(NOT_FOUND).body(exceptionDto);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    ResponseEntity<ExceptionDto> handleException(DuplicateKeyException ex) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setErrorMessage(ex.getLocalizedMessage());
        exceptionDto.setErrorCode(40901);
        exceptionDto.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(409).body(exceptionDto);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionDto> handleMessageNotReadableException(HttpMessageNotReadableException ex) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setErrorMessage(ex.getMessage());
        exceptionDto.setErrorCode(400);
        exceptionDto.setTimestamp(LocalDateTime.now());
        return ResponseEntity.badRequest().body(exceptionDto);
    }

    @ExceptionHandler(Throwable.class)
    ResponseEntity<ExceptionDto> handleException(Throwable e) {
        log.error(e.getMessage(), e);
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setErrorMessage(e.getMessage());
        exceptionDto.setErrorCode(500);
        exceptionDto.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(exceptionDto);
    }
}
