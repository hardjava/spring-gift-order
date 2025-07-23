package gift.dto;

import gift.domain.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public record ProductWithPageResponseDto(
        List<ProductResponseDto> content,
        PaginationMetadataDto paginationMetadata
) {
    public static ProductWithPageResponseDto from(Page<Product> list) {

        PaginationMetadataDto metadataDto = new PaginationMetadataDto(
                list.getPageable().getPageNumber() + 1,
                list.getPageable().getPageSize(),
                list.getTotalPages(),
                list.getTotalElements()
        );

        return new ProductWithPageResponseDto(
                list.getContent()
                        .stream()
                        .map(ProductResponseDto::from)
                        .toList(),
                metadataDto
        );
    }
}
