package com.example.demo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record PageRequestDto(

    @Min(value = 0, message = "페이지는 0 이상이어야 합니다.")
    int page,

    @Min(value = 1, message = "사이즈는 1 이상이어야 합니다.")
    @Max(value = 20, message = "사이즈는 20 이하이어야 합니다.")
    int size
) {
}
