package gift.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderRequestDto(
        @NotNull @Min(0) Long optionId,
        @NotNull @Min(1) Integer quantity,
        String message
) {
}
