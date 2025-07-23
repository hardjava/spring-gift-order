package gift.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PurchaseOptionRequestDto(
        @NotNull(message = "수량을 입력해주세요")
        @Positive(message = "수량은 음수가 될 수 없습니다")
        @Max(value = 100_000_000, message = "수량은 1억개를 넘을 수 없습니다")
        @Min(value = 1, message = "수량은 1개 이상이어야 합니다")
        Long quantity
) {

}
