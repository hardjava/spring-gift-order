package gift.dto;

import jakarta.validation.constraints.*;

public record OptionRequestDto(
        @NotBlank(message = "옵션 이름은 필수 입력 사항입니다.")
        @Size(max = 50, message = "옵션 이름은 최대 50자까지 입력할 수 있습니다.")
        @Pattern(
                regexp = "^[\\p{L}\\p{N} ()\\[\\]+\\-\\&/_]*$",
                message = "옵션 이름은 허용된 특수문자만 사용 가능합니다."
        )
        String name,

        @Min(value = 1, message = "옵션 수량은 최소 1 이상이어야 합니다.")
        @Max(value = 99999999, message = "옵션 수량은 최대 99,999,999까지 입력할 수 있습니다.")
        int quantity
) {
}