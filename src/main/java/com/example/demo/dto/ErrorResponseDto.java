package com.example.demo.dto;

import java.util.Map;

public class ErrorResponseDto {
  private String type;
  private String title;
  private int status;
  private String detail;
  private String instance;
  private Map<String, String> errors;

  public ErrorResponseDto(String type, String title, int status, String detail, String instance, Map<String, String> errors) {
    this.type = type;
    this.title = title;
    this.status = status;
    this.detail = detail;
    this.instance = instance;
    this.errors = errors;
  }

  public String getType() {
    return type;
  }

  public String getTitle() {
    return title;
  }

  public int getStatus() {
    return status;
  }

  public String getDetail() {
    return detail;
  }

  public String getInstance() {
    return instance;
  }

  public Map<String, String> getErrors() {
    return errors;
  }
}

