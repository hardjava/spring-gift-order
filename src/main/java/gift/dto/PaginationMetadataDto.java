package gift.dto;

public record PaginationMetadataDto(
        int page,
        int limit,
        int totalPage,
        Long totalCount
) {
}
