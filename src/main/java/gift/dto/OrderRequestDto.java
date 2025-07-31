package gift.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderRequestDto(
        @NotNull(message = "옵션 ID는 필수 입니다.")
        @Min(value = 0, message = "옵션 ID는 0 이상의 값이어야 합니다.")
        Long optionId,

        @NotNull(message = "수량은 필수입니다.")
        @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다.") Integer quantity,
        String message
) {
}
