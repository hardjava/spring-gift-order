package gift.wish.dto;

import gift.wish.entity.Wish;

public record WishResponseDto(
        Long id,
        Long productId,
        String productName,
        int productPrice
) {
    public static WishResponseDto of(Wish wish) {
        return new WishResponseDto(
                wish.getId(),
                wish.getProduct().getId(),
                wish.getProduct().getName(),
                wish.getProduct().getPrice()
        );
    }
}
