package ru.practicum.server.exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Контроллер для обработки кастомных исключений
 * */
@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(NotFoundError.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFoundException(final RuntimeException e) {
        log.trace("Exception: " + HttpStatus.NOT_FOUND.name());
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler(BadRequestError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleEntityBadRequestException(final RuntimeException e) {
        log.trace("Exception: " + HttpStatus.BAD_REQUEST.name());
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler(ForbiddenError.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleEntityForbiddenException(final RuntimeException e) {
        log.trace("Exception: " + HttpStatus.FORBIDDEN.name());
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler(ConflictError.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEntityConflictException(final RuntimeException e) {
        log.trace("Exception: " + HttpStatus.CONFLICT.name());
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler(InternetServerError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleEntityServerException(final RuntimeException e) {
        log.trace("Exception: " + HttpStatus.INTERNAL_SERVER_ERROR.name());
        return new ErrorResponse(
                e.getMessage()
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