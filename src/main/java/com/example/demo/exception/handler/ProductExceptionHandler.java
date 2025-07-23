package com.example.demo.exception.handler;

import com.example.demo.controller.product.ProductController;
import com.example.demo.dto.ErrorResponseDto;
import com.example.demo.exception.DuplicateOptionException;
import com.example.demo.exception.OptionNotFoundException;
import com.example.demo.exception.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice(assignableTypes = {ProductController.class})
public class ProductExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponseDto> handleIllegalArgument(
      IllegalArgumentException ex,
      HttpServletRequest request
  ) {
    ErrorResponseDto errorResponseDto = new ErrorResponseDto(
        "https://example.com/product-not-found",
        "Product Not Found",
        HttpStatus.NOT_FOUND.value(),
        ex.getMessage(),
        request.getRequestURI(),
        null
    );

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
  }

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handleProductNotFound(
      ProductNotFoundException ex,
      HttpServletRequest request
  ){
    ErrorResponseDto dto = new ErrorResponseDto(
        "https://example.com/product-not-found",
        "Product Not Found",
        HttpStatus.NOT_FOUND.value(),
        ex.getMessage(),
        request.getRequestURI(),
        null
    );
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
  }

  @ExceptionHandler(DuplicateOptionException.class)
  public ResponseEntity<ErrorResponseDto> handleDuplicateOption(
      DuplicateOptionException ex,
      HttpServletRequest request
  ) {
    ErrorResponseDto dto = new ErrorResponseDto(
        "https://example.com/duplicate-option",
        "Duplicate Option",
        HttpStatus.CONFLICT.value(),
        ex.getMessage(),
        request.getRequestURI(),
        null
    );
    return ResponseEntity.status(HttpStatus.CONFLICT).body(dto);
  }

  @ExceptionHandler(OptionNotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handleOptionNotFound(
      OptionNotFoundException ex,
      HttpServletRequest request
  ) {
    ErrorResponseDto dto = new ErrorResponseDto(
        "https://example.com/option-not-found",
        "Option Not Found",
        HttpStatus.NOT_FOUND.value(),
        ex.getMessage(),
        request.getRequestURI(),
        null
    );
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
  }
}
