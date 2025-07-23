package gift.wish.service;

import gift.wish.dto.WishResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishService {
    WishResponseDto createWish(Long memberId, Long productId);
    Page<WishResponseDto> findAllWishesByMemberId(Long memberId, Pageable pageable);
    void deleteWish(Long memberId, Long wishId);
}
