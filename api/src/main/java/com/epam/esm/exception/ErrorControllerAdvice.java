package com.epam.esm.exception;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ErrorControllerAdvice {
    /**
     * Run if url is not supported (404)
     */
    @ExceptionHandler
    public ResponseEntity<ExceptionDto> handle(NoHandlerFoundException ex) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setErrorMessage(ex.getMessage());
        exceptionDto.setErrorCode(NOT_FOUND.value());
        exceptionDto.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(NOT_FOUND).body(exceptionDto);
    }

    /**
     * Handles errors from @Validated
     */
    @ExceptionHandler
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
                    exceptionDto.setErrorCode(40001);
                    exceptionDto.setTimestamp(LocalDateTime.now());
                    exceptionDtoList.add(exceptionDto);
                });
        return ResponseEntity.badRequest().body(exceptionDtoList);
    }

    /**
     * Handles following cases:
     * <ul>
     *  <li>Enum request parameters don't match enum members</li>
     *  <li>A path variable can't be parsed to an integer</li>
     * </ul>
     */
    @ExceptionHandler
    public ResponseEntity<ExceptionDto> handle(MethodArgumentTypeMismatchException ex) {
        String parameterName = ex.getName();
        String message;
        if (ex.getCause().getClass().equals(ConversionFailedException.class)) {
            String enumValues = Arrays.stream(Objects.requireNonNull(ex.getRequiredType()).getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            message = String.format("Parameter '%s' must be either null or one of the following values: %s",
                    parameterName, enumValues);
        } else {
            message = String.format("Parameter '%s' is wrong. %s", parameterName, ex.getMessage());
        }
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setErrorMessage(message);
        exceptionDto.setErrorCode(40001);
        exceptionDto.setTimestamp(LocalDateTime.now());
        return ResponseEntity.badRequest().body(exceptionDto);
    }

    /**
     * Handles errors from @Valid
     */
    @ExceptionHandler
    public ResponseEntity<List<ExceptionDto>> handle(MethodArgumentNotValidException ex) {
        List<ExceptionDto> exceptionDtoList = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            String fieldName = ((FieldError) error).getField();
            ExceptionDto exceptionDto = new ExceptionDto();
            exceptionDto.setErrorMessage(String.format("%s - %s", fieldName, errorMessage));
            exceptionDto.setErrorCode(4001);
            exceptionDto.setTimestamp(LocalDateTime.now());
            exceptionDtoList.add(exceptionDto);
        });
        return ResponseEntity.badRequest().body(exceptionDtoList);
    }

    /**
     * Handles not valid parameters "page", "size" when {@link com.epam.esm.dto.CustomPage} is being created
     */
    @ExceptionHandler
    public ResponseEntity<List<ExceptionDto>> handle(BindException ex) {
        List<ExceptionDto> exceptionDtoList = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            String fieldName = ((FieldError) error).getField();
            ExceptionDto exceptionDto = new ExceptionDto();
            exceptionDto.setErrorMessage(String.format("%s - %s", fieldName, errorMessage));
            exceptionDto.setErrorCode(40001);
            exceptionDto.setTimestamp(LocalDateTime.now());
            exceptionDtoList.add(exceptionDto);
        });
        return ResponseEntity.badRequest().body(exceptionDtoList);
    }

    /**
     * Handles custom exception ResourceNotFoundException
     */
    @ExceptionHandler
    public ResponseEntity<ExceptionDto> handle(ResourceNotFoundException ex) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setErrorMessage(ex.getMessage());
        exceptionDto.setErrorCode(40401);
        exceptionDto.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(NOT_FOUND).body(exceptionDto);
    }

    /**
     * Handles custom exception ResourceAlreadyExistException
     */
    @ExceptionHandler
    public ResponseEntity<ExceptionDto> handle(ResourceAlreadyExistException ex) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setErrorMessage(ex.getMessage());
        exceptionDto.setErrorCode(40001);
        exceptionDto.setTimestamp(LocalDateTime.now());
        return ResponseEntity.badRequest().body(exceptionDto);
    }

    /**
     * Handles duplicate key error
     */
    @ExceptionHandler
    public ResponseEntity<ExceptionDto> handle(DataIntegrityViolationException ex) {
        PSQLException cause = (PSQLException) ex.getCause().getCause();
        String errorMessage = cause.getMessage();
        errorMessage = errorMessage.replaceAll("\\s\"\\w+\"\n\\s", ".");
        errorMessage = errorMessage.replaceAll("Key \\(\\w+\\)", "value");
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setErrorMessage(errorMessage);
        exceptionDto.setErrorCode(40001);
        exceptionDto.setTimestamp(LocalDateTime.now());
        return ResponseEntity.badRequest().body(exceptionDto);
    }

    /**
     * Run if JWT token is expired or invalid
     */
    @ExceptionHandler
    public ResponseEntity<ExceptionDto> handle(AuthenticationException ex) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setErrorMessage(ex.getMessage());
        exceptionDto.setErrorCode(40101);
        exceptionDto.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionDto);
    }

    /**
     * Run if a user doesn't have required role
     */
    @ExceptionHandler
    public ResponseEntity<ExceptionDto> handle(AccessDeniedException ex) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setErrorMessage(ex.getMessage());
        exceptionDto.setErrorCode(40301);
        exceptionDto.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exceptionDto);
    }

    /**
     * Handles CustomAccessDeniedException
     */
    @ExceptionHandler
    public ResponseEntity<ExceptionDto> handle(CustomAccessDeniedException ex) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setErrorMessage(ex.getMessage());
        exceptionDto.setErrorCode(40301);
        exceptionDto.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exceptionDto);
    }

    /**
     * Catches all unexpected exceptions
     */
    @ExceptionHandler
    public ResponseEntity<ExceptionDto> handle(Throwable ex) {
        log.error(ex.getMessage(), ex);
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setErrorMessage(ex.getMessage());
        exceptionDto.setErrorCode(50001);
        exceptionDto.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(exceptionDto);
    }
}
