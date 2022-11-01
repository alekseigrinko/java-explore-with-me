package ru.practicum.server.exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");



    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEntityNotFoundException(final EntityNotFoundException e) {
        log.trace("Exception: " + HttpStatus.NOT_FOUND.name());
        return new ApiError(
                HttpStatus.FORBIDDEN.name(),
                "The required object was not found.",
                e.getMessage(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleEntityBadRequestException(final HttpClientErrorException.BadRequest e) {
        log.trace("Exception: " + HttpStatus.BAD_REQUEST.name());
        return new ApiError(
                HttpStatus.BAD_REQUEST.name(),
                "For the requested operation the conditions are not met.",
                e.getMessage(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleEntityForbiddenException(final HttpClientErrorException.Forbidden e) {
        log.trace("Exception: " + HttpStatus.FORBIDDEN.name());
        return new ApiError(
                HttpStatus.FORBIDDEN.name(),
                "For the requested operation the conditions are not met.",
                e.getMessage(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEntityConflictException(final HttpClientErrorException.Conflict e) {
        log.trace("Exception: " + HttpStatus.CONFLICT.name());
        return new ApiError(
                HttpStatus.CONFLICT.name(),
                "Integrity constraint has been violated",
                e.getMessage(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleEntityServerException(final HttpServerErrorException.InternalServerError e) {
        log.trace("Exception: " + HttpStatus.INTERNAL_SERVER_ERROR.name());
        return new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                "Error occurred",
                e.getMessage(),
                LocalDateTime.now().format(formatter));
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