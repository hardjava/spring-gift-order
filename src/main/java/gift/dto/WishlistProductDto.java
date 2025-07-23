package gift.dto;

public record WishlistProductDto(
        Long id,
        String name,
        int price,
        String imageUrl,
        int quantity
) {}

