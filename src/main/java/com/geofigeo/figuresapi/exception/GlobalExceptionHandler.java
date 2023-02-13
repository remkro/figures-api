package com.geofigeo.figuresapi.exception;

import com.geofigeo.figuresapi.dto.StatusDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({EmailAlreadyTakenException.class})
    public ResponseEntity<StatusDto> handleEmailAlreadyTakenException(Exception e) {
        StatusDto response = new StatusDto(e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler({ShapeNotFoundException.class})
    public ResponseEntity<StatusDto> handleShapeNotFoundException(Exception e) {
        StatusDto response = new StatusDto(e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler({ShapeNotSupportedException.class})
    public ResponseEntity<StatusDto> handleShapeNotSupportedException(Exception e) {
        StatusDto response = new StatusDto(e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler({UserAlreadyTakenException.class})
    public ResponseEntity<StatusDto> handleUserAlreadyTakenException(Exception e) {
        StatusDto response = new StatusDto(e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}
