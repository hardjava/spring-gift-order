package com.example.demo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ForbiddenValidator.class)
public @interface NoForbiddenWords {
  String message() default "금지된 단어가 포함되어 있습니다.";
  String[] words() default {"카카오"};
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
