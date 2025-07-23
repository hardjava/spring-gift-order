package com.example.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class ForbiddenValidator implements ConstraintValidator<NoForbiddenWords, String> {

  private List<String> forbiddenWords;

  @Override
  public void initialize(NoForbiddenWords annotation){
    this.forbiddenWords = Arrays.asList(annotation.words());
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if(value == null) return true;
    for(String word : forbiddenWords){
      if(value.contains(word)){
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
            "금지된 단어인 '" + word + "'가 포함되어 있습니다."
          ).addConstraintViolation();
          return false;
      }
    }
    return true;
  }
}
