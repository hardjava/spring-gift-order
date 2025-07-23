package gift.service;

import gift.dto.CreateWishRequestDto;
import gift.dto.WishPageDto;
import gift.dto.WishResponseDto;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishService {

    WishResponseDto createWish(CreateWishRequestDto requestDto, Long memberId);

    WishPageDto findMemberWishes(Long memberId, Pageable pageable);

    WishResponseDto updateMemberWishQuantityByProductId(Long quantity, Long productId,
            Long memberId);

    void deleteMemberWishByProductId(Long productId, Long memberId);
}