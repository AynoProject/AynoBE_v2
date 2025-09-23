package com.ayno.aynobe.config.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus status;

    public CustomException(String errorCode, String message, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    // --- 4xx ---
    public static CustomException badRequest(String message) {
        return new CustomException("REQ.BAD_REQUEST", message, HttpStatus.BAD_REQUEST);
    }
    public static CustomException validation(String message) {
        return new CustomException("REQ.VALIDATION", message, HttpStatus.BAD_REQUEST);
    }
    public static CustomException typeMismatch(String message) {
        return new CustomException("REQ.TYPE_MISMATCH", message, HttpStatus.BAD_REQUEST);
    }
    public static CustomException missingParam(String message) {
        return new CustomException("REQ.MISSING_PARAM", message, HttpStatus.BAD_REQUEST);
    }
    public static CustomException invalidJson(String message) {
        return new CustomException("REQ.INVALID_JSON", message, HttpStatus.BAD_REQUEST);
    }

    public static CustomException unauthorized(String message) {
        return new CustomException("AUTH.UNAUTHORIZED", message, HttpStatus.UNAUTHORIZED);
    }
    public static CustomException forbidden(String message) {
        return new CustomException("AUTH.FORBIDDEN", message, HttpStatus.FORBIDDEN);
    }

    public static CustomException notFound(String message) {
        return new CustomException("DATA.NOT_FOUND", message, HttpStatus.NOT_FOUND);
    }

    public static CustomException methodNotAllowed(String message) {
        return new CustomException("REQ.METHOD_NOT_ALLOWED", message, HttpStatus.METHOD_NOT_ALLOWED);
    }
    public static CustomException unsupportedMediaType(String message) {
        return new CustomException("REQ.UNSUPPORTED_MEDIA_TYPE", message, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    public static CustomException duplicate(String message) {
        return new CustomException("DATA.DUPLICATE", message, HttpStatus.CONFLICT);
    }
    public static CustomException constraint(String message) {
        return new CustomException("DATA.CONSTRAINT", message, HttpStatus.CONFLICT);
    }
    public static CustomException conflict(String message) {
        return new CustomException("DATA.CONFLICT", message, HttpStatus.CONFLICT);
    }

    public static CustomException tooLarge(String message) {
        return new CustomException("UPLOAD.TOO_LARGE", message, HttpStatus.PAYLOAD_TOO_LARGE);
    }
    public static CustomException rateLimit(String message) {
        return new CustomException("REQ.RATE_LIMIT", message, HttpStatus.TOO_MANY_REQUESTS);
    }
    public static CustomException unprocessable(String message) {
        return new CustomException("REQ.UNPROCESSABLE", message, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    // --- 5xx ---
    public static CustomException internal(String message) {
        return new CustomException("SYS.INTERNAL", message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
