package com.example.demo.exception.handler;

import com.example.demo.controller.WishController;
import com.example.demo.dto.ErrorResponseDto;
import com.example.demo.exception.DuplicateWishException;
import com.example.demo.exception.WishNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {WishController.class})
public class WishExceptionHandler {

  @ExceptionHandler(DuplicateWishException.class)
  public ResponseEntity<ErrorResponseDto> handleDuplicateWish(
      DuplicateWishException ex,
      HttpServletRequest request
  ) {
    ErrorResponseDto errorResponseDto = new ErrorResponseDto(
        "https://example.com/duplicate-wish",
        "Conflict",
        HttpStatus.CONFLICT.value(),
        ex.getMessage(),
        request.getRequestURI(),
        null
    );

    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(errorResponseDto);
  }

  @ExceptionHandler(WishNotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handleWishNotFound(
      WishNotFoundException ex,
      HttpServletRequest request
  ) {
    ErrorResponseDto errorResponseDto = new ErrorResponseDto(
        "https://example.com/wish-not-found",
        "Wish Not Found",
        HttpStatus.NOT_FOUND.value(),
        ex.getMessage(),
        request.getRequestURI(),
        null
    );

    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(errorResponseDto);
  }
}
