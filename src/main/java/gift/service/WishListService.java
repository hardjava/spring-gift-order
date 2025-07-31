package gift.service;

import gift.domain.*;
import gift.dto.WishSummaryWithPageResponseDto;
import gift.repository.ProductRepository;
import gift.repository.WishListRepository;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class WishListService {
    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;

    public WishListService(WishListRepository wishListRepository, ProductRepository productRepository) {
        this.wishListRepository = wishListRepository;
        this.productRepository = productRepository;
    }

    public WishSummaryWithPageResponseDto findAllWishSummaryByMemberId(WishListPaginationInfo paginationInfo) {
        Page<WishSummary> findWishSummary = wishListRepository.findWishSummaryByMemberId(
                paginationInfo.getMemberId(),
                paginationInfo.getSearch(),
                paginationInfo.getPageable()
        );

        return WishSummaryWithPageResponseDto.from(findWishSummary);
    }

    @Transactional
    public void saveWish(Member member, Long productId) {
        Product findProduct = productRepository.findProductByIdOrElseThrow(productId);
        Wish wish = new Wish(member, findProduct);

        wishListRepository.save(wish);
    }

    @Transactional
    public void deleteWish(Long memberId, Long productId) {
        if (!wishListRepository.existsWishByMemberIdAndProductId(memberId, productId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 상품을 찾을 수 없습니다.");
        }

        wishListRepository.deleteWishByMemberIdAndProductId(memberId, productId);
    }

}
