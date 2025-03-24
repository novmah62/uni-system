package com.drewsec.user_central.exception;

import com.drewsec.user_central.dto.response.ApiResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> resourceNotFoundExceptionHandler(ResourceNotFoundException exception) {
        ApiResponse<String> apiResponse = new ApiResponse<>(404, "Resource Not Found Exception", exception.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<String>> dataIntegrityViolationExceptionHandler(DataIntegrityViolationException exception) {
        if (exception.getCause() instanceof ConstraintViolationException consVioExp) {
            String constraintName = consVioExp.getConstraintName();
            if (constraintName != null && constraintName.toLowerCase().contains("email"))
                return new ResponseEntity<>(new ApiResponse<>(404, "Email already in use", constraintName), HttpStatus.BAD_REQUEST);
        }
        ApiResponse<String> apiResponse = new ApiResponse<>(404, "Data Integrity Violation Exception", exception.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> methodArgsNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        Map<String, String> resp = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error)-> {
            String fieldName = ((FieldError)error).getField();
            String message = error.getDefaultMessage();
            resp.put(fieldName, message);
        });
        ApiResponse<Map<String, String>> apiResponse = new ApiResponse<>(404, ex.getMessage(), resp);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_GATEWAY);
    }

}
