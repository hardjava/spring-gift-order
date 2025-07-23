package gift.dto;

import jakarta.validation.constraints.NotNull;

public record WishRequestDto(
        @NotNull(message = "상품 번호는 필수 입력입니다.")
        Long productId
) {
}
