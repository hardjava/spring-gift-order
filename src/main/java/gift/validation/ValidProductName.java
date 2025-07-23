package gift.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Constraint(validatedBy = {})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "상품 이름은 비어 있을 수 없습니다.")
@Size(max = 15, message = "상품 이름은 공백 포함 최대 15자까지 입력할 수 있습니다.")
@Pattern(
        regexp = "^[\\p{L}\\p{N}\\s\\(\\)\\[\\]\\+\\-\\&/_]*$",
        message = "상품 이름에는 ( ), [ ], +, -, &, /, _ 의 특수 문자만 사용할 수 있습니다."
)
public @interface ValidProductName {
    String message() default "유효하지 않은 상품 이름입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
