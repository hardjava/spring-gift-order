package gift.dto;

import gift.validation.ValidProductRequest;

import java.util.List;

@ValidProductRequest
public record CreateProductRequestDto(
        String name,

        Long price,

        String imageUrl,

        List<OptionRequestDto> options
) {
}
