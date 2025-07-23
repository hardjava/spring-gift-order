package gift.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateWishRequestDto(
        @NotNull(message = "상품 아이디를 입력해 주세요")
        Long productId,
        @NotNull(message = "수량을 입력해 주세요")
        @Positive(message = "수량은 양수만 가능합니다")
        Long quantity
) {

}