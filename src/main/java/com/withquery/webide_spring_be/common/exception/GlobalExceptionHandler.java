package com.withquery.webide_spring_be.common.exception;

import com.withquery.webide_spring_be.common.dto.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

<<<<<<< HEAD
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> handleRuntimeException(
		RuntimeException ex, WebRequest request) {
		log.warn("Runtime exception: {}", ex.getMessage());
=======
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        log.warn("Validation exception: {}", ex.getMessage());
        
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("유효하지 않은 요청입니다.");
        
        ErrorResponse errorResponse = new ErrorResponse(
            "BAD_REQUEST", 
            errorMessage
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
>>>>>>> main

		ErrorResponse errorResponse = new ErrorResponse(
			"BAD_REQUEST",
			ex.getMessage()
		);

<<<<<<< HEAD
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
		IllegalArgumentException ex, WebRequest request) {
		log.warn("Illegal argument exception: {}", ex.getMessage());

		ErrorResponse errorResponse = new ErrorResponse(
			"INVALID_ARGUMENT",
			ex.getMessage()
		);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGlobalException(
		Exception ex, WebRequest request) {
		log.error("Unexpected error occurred: {}", ex.getMessage(), ex);

		ErrorResponse errorResponse = new ErrorResponse(
			"INTERNAL_SERVER_ERROR",
			"서버 내부 오류가 발생했습니다."
		);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
=======
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException ex, WebRequest request) {
        log.warn("Runtime exception: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            "BAD_REQUEST", 
            ex.getMessage()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
            "INTERNAL_SERVER_ERROR", 
            "서버 내부 오류가 발생했습니다."
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
>>>>>>> main
} 