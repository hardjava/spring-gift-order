package gift.dto;

import java.util.List;
import org.springframework.data.domain.Page;

public record ProductPageDto(
        List<ProductResponseDto> contents,
        int pageNum,
        int totalPageNum
) {
    public ProductPageDto(Page<ProductResponseDto> page){
        this(page.getContent(), page.getNumber(), page.getTotalPages());
    }
}
