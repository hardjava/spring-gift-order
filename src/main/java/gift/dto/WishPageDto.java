package gift.dto;

import gift.entity.Wish;
import java.util.List;
import org.springframework.data.domain.Page;

public record WishPageDto(
        List<WishResponseDto> content,
        int pageNum,
        int totalPageNum
) {
    public WishPageDto(Page<WishResponseDto> page){
        this(page.getContent(), page.getNumber(), page.getTotalPages());
    }
}
