package gift.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "이메일은 비어있을 수 없습니다.")
@Email(message = "이메일 형식으로 입력해 주세요.")
public @interface ValidMemberEmail {
    String message() default "유효하지 않은 이메일 입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
