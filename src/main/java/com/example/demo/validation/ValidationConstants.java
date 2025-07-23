package com.example.demo.validation;

public class ValidationConstants {
  private ValidationConstants() {}

  public static final String PRODUCT_NAME_NOT_BLANK = "상품 이름을 공백일 수 없습니다.";
  public static final String PRODUCT_NAME_SIZE = "상품의 이름은 공백 포함 15자 이하로 입력해주세요.";
  public static final String NAME_PATTERN_MESSAGE = "이름에는 (), [], +, -, &, /, _ 외 특수문자는 사용할 수 없습니다.";
  public static final String NAME_REGEX = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]*$";

  public static final String OPTION_NAME_NOT_BLANK = "옵션 이름은 공백일 수 없습니다.";
  public static final String OPTION_NAME_SIZE = "옵션의 이름은 공백 포함 50자 이하로 입력해주세요.";
}
