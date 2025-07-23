package gift.option.dto;

import jakarta.validation.constraints.*;

public record OptionUpdateRequestDto(

        @NotBlank(message = "옵션명은 필수입니다.")
        @Size(max = 50, message = "옵션명은 최대 50자까지 입력할 수 있습니다.")
        @Pattern(
                regexp = "^[\\p{L}0-9 \\[\\]\\(\\)\\+\\-\\&/_]+$",
                message = "허용되지 않는 특수문자가 포함되어 있습니다."
        )
        String name,

        @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다.")
        @Max(value = 100_000_000, message = "수량은 1억 개 미만이어야 합니다.")
        int quantity
) { }
