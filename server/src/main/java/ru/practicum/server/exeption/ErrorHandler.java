package ru.practicum.server.exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Контроллер для обработки кастомных исключений
 * */
@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler(NotFoundError.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public NotFoundErrorCustom handleEntityNotFoundException(final RuntimeException e) {
        log.warn("Exception: " + HttpStatus.NOT_FOUND.name());
        return new NotFoundErrorCustom(
                String.valueOf(HttpStatus.NOT_FOUND),
                "The required object was not found.",
                e.getMessage(),
                LocalDateTime.now().format(formatter),
                e.getStackTrace()
        );
    }

    @ExceptionHandler(BadRequestError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestErroCustom handleEntityBadRequestException(final RuntimeException e) {
        log.warn("Exception: " + HttpStatus.BAD_REQUEST.name());
        return new BadRequestErroCustom(
                String.valueOf(HttpStatus.BAD_REQUEST),
                "For the requested operation the conditions are not met.",
                e.getMessage(),
                LocalDateTime.now().format(formatter),
                e.getStackTrace()
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestErroCustom handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        log.warn("Exception: " + HttpStatus.BAD_REQUEST.name());
        return new BadRequestErroCustom(
                String.valueOf(HttpStatus.BAD_REQUEST),
                "For the requested operation the conditions are not met.",
                e.getMessage(),
                LocalDateTime.now().format(formatter),
                e.getStackTrace()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestErroCustom handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.warn("Exception: " + HttpStatus.BAD_REQUEST.name());
        return new BadRequestErroCustom(
                String.valueOf(HttpStatus.BAD_REQUEST),
                "For the requested operation the conditions are not met.",
                e.getMessage(),
                LocalDateTime.now().format(formatter),
                e.getStackTrace()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestErroCustom handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        log.warn("Exception: " + HttpStatus.BAD_REQUEST.name());
        return new BadRequestErroCustom(
                String.valueOf(HttpStatus.BAD_REQUEST),
                "For the requested operation the conditions are not met.",
                e.getMessage(),
                LocalDateTime.now().format(formatter),
                e.getStackTrace()
        );
    }

    @ExceptionHandler(ForbiddenError.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ForbiddenErrorCustom handleEntityForbiddenException(final RuntimeException e) {
        log.warn("Exception: " + HttpStatus.FORBIDDEN.name());
        return new ForbiddenErrorCustom(
                String.valueOf(HttpStatus.FORBIDDEN),
                "For the requested operation the conditions are not met.",
                e.getMessage(),
                LocalDateTime.now().format(formatter),
                e.getStackTrace()
        );
    }

    @ExceptionHandler(ConflictError.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ConflictErrorCustom handleEntityConflictException(final RuntimeException e) {
        log.warn("Exception: " + HttpStatus.CONFLICT.name());
        return new ConflictErrorCustom(
                String.valueOf(HttpStatus.CONFLICT),
                "Integrity constraint has been violated",
                e.getMessage(),
                LocalDateTime.now().format(formatter),
                e.getStackTrace()
        );
    }

    @ExceptionHandler(InternetServerError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public InternetServerErrorCustom handleEntityServerException(final RuntimeException e) {
        log.warn("Exception: " + HttpStatus.INTERNAL_SERVER_ERROR.name());
        return new InternetServerErrorCustom(
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR),
                "Error occurred",
                e.getMessage(),
                LocalDateTime.now().format(formatter),
                e.getStackTrace()
        );
    }

    public class ErrorResponse {

        String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }
}