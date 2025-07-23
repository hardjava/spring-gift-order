package gift.dto;

import gift.domain.WishSummary;
import org.springframework.data.domain.Page;

import java.util.List;

public record WishSummaryWithPageResponseDto(
        List<WishSummaryResponseDto> content,
        PaginationMetadataDto paginationMetadata
) {
    public static WishSummaryWithPageResponseDto from(Page<WishSummary> list) {

        PaginationMetadataDto metadataDto = new PaginationMetadataDto(
                list.getPageable().getPageNumber() + 1,
                list.getPageable().getPageSize(),
                list.getTotalPages(),
                list.getTotalElements()
        );

        return new WishSummaryWithPageResponseDto(
                list.getContent()
                        .stream()
                        .map(WishSummaryResponseDto::from)
                        .toList(),
                metadataDto
        );
    }
}
