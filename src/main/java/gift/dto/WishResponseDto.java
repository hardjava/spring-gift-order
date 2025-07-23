package gift.dto;

public record WishResponseDto(
        ProductResponseDto productResponseDto,
        Long quantity
) {

}