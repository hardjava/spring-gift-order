package com.example.demo.exception;

public class DuplicateWishException extends RuntimeException {

  public DuplicateWishException(String message) {
    super(message);
  }
}
