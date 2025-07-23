package com.example.demo.exception.handler;

import com.example.demo.controller.user.UserPageController;
import com.example.demo.dto.ErrorResponseDto;
import com.example.demo.exception.InvalidLoginException;
import com.example.demo.exception.InvalidPageRequestException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice(assignableTypes = {UserPageController.class})
public class PageExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public String handleValidationException(
      MethodArgumentNotValidException ex,
      RedirectAttributes redirectAttributes
  ) {
    String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                            .map(error -> error.getDefaultMessage())
                            .findFirst()
                            .orElse("입력값이 올바르지 않습니다.");

    redirectAttributes.addFlashAttribute("signupError", errorMessage);
    return "redirect:/signup-page";
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public String handleIllegalArgument(
      IllegalArgumentException ex,
      RedirectAttributes redirectAttributes
  ) {
    redirectAttributes.addFlashAttribute("signupError", ex.getMessage());
    return "redirect:/signup-page";
  }

  @ExceptionHandler(InvalidLoginException.class)
  public String handleInvalidLogin(
      InvalidLoginException ex,
      RedirectAttributes redirectAttributes
  ) {
    redirectAttributes.addFlashAttribute("loginError", ex.getMessage());
    return "redirect:/login-page";
  }

  @ExceptionHandler(InvalidPageRequestException.class)
  public ResponseEntity<ErrorResponseDto> handleInvalidPage(
      InvalidPageRequestException ex,
      HttpServletRequest request
  ){
    ErrorResponseDto dto = new ErrorResponseDto(
        "https://example.com/invalid-page-parameters",
        "Invalid Page Parameters",
        HttpStatus.BAD_REQUEST.value(),
        ex.getMessage(),
        request.getRequestURI(),
        null
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
  }
}
