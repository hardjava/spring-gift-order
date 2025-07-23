package com.example.demo.dto.product;

import com.example.demo.validation.ValidationConstants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProductOptionRequestDto(

    @NotBlank(message = ValidationConstants.OPTION_NAME_NOT_BLANK)
    @Size(max = 50, message = ValidationConstants.OPTION_NAME_SIZE)
    @Pattern(
        regexp = ValidationConstants.NAME_REGEX,
        message = ValidationConstants.NAME_PATTERN_MESSAGE
    )
    String name,

    @Min(value = 1, message = "옵선 수량은 최대 1 이상이어야 합니다.")
    @Max(value = 99_999_999, message = "옵션 수량은 최대 1억 미만 이어야 합니다.")
    int quantity
) {
}
