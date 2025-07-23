package com.example.demo.exception;

public class InvalidPageRequestException extends RuntimeException {

  public InvalidPageRequestException(String message) {
    super(message);
  }
}
