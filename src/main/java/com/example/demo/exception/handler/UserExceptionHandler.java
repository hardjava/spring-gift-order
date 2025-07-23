package com.example.demo.exception.handler;

import com.example.demo.controller.user.UserController;
import com.example.demo.dto.ErrorResponseDto;
import com.example.demo.exception.InvalidLoginException;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {UserController.class})
public class UserExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDto> handleValidationErrors(
      MethodArgumentNotValidException ex,
      HttpServletRequest request
  ) {
    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult()
      .getFieldErrors()
      .forEach(error ->
          errors.put(error.getField(), error.getDefaultMessage())
      );

    ErrorResponseDto errorResponseDto = new ErrorResponseDto(
        "https://example.com/validation-error",
        "Validation Failed",
        HttpStatus.BAD_REQUEST.value(),
        "입력값이 유효하지 않습니다.",
        request.getRequestURI(),
        errors
    );

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(errorResponseDto);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponseDto> handleIllegalArgument(
      IllegalArgumentException ex,
      HttpServletRequest request
  ){
    ErrorResponseDto errorResponseDto = new ErrorResponseDto(
        "https://example.com/not-found",
        "Resource Not Found",
        HttpStatus.NOT_FOUND.value(),
        ex.getMessage(),
        request.getRequestURI(),
        null
    );
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(errorResponseDto);
    }
  
  @ExceptionHandler(InvalidLoginException.class)
  public ResponseEntity<ErrorResponseDto> handleInvalidLogin(
      InvalidLoginException ex,
      HttpServletRequest request
  ){
    ErrorResponseDto errorResponseDto = new ErrorResponseDto(
        "https://example.com/unauthorized",
        "Unauthorized",
        HttpStatus.UNAUTHORIZED.value(),
        ex.getMessage(),
        request.getRequestURI(),
        null
    );

    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(errorResponseDto);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handleUserNotFound(
      UserNotFoundException ex,
      HttpServletRequest request
  ) {
    ErrorResponseDto errorResponseDto = new ErrorResponseDto(
        "https://example.com/user-not-found",
        "User Not Found",
        HttpStatus.NOT_FOUND.value(),
        ex.getMessage(),
        request.getRequestURI(),
        null
    );

    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(errorResponseDto);
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ErrorResponseDto> handleUnauthorized(
      UnauthorizedException ex,
      HttpServletRequest request
  ) {
    ErrorResponseDto dto = new ErrorResponseDto(
        "https://example.com/unauthorized",
        "Unauthorized",
        HttpStatus.UNAUTHORIZED.value(),
        ex.getMessage(),
        request.getRequestURI(),
        null
    );
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(dto);
  }
}
