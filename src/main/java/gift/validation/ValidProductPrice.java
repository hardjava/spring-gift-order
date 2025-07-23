package gift.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

@Constraint(validatedBy = {})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@NotNull(message = "가격은 필수 입력입니다.")
@Min(value = 0, message = "가격은 0 이상이어야 합니다.")
public @interface ValidProductPrice {
    String message() default "유효하지 않은 가격입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
