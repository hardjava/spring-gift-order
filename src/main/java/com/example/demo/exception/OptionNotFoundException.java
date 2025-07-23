package com.example.demo.exception;

public class OptionNotFoundException extends RuntimeException {

  public OptionNotFoundException(String message) {
    super(message);
  }
}
