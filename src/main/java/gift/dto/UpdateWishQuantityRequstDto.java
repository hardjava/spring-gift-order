package gift.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateWishQuantityRequstDto(
        @NotNull(message = "상품 수량을 입력해 주세요")
        @PositiveOrZero(message = "수량에 음수는 입력할 수 없습니다")
        Long quantity) {

}
