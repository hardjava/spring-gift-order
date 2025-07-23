package gift.service;

import gift.domain.Member;
import gift.enums.Role;
import gift.repository.ProductRepository;
import gift.repository.WishListRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class WishListServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private WishListRepository wishListRepository;

    @InjectMocks
    private WishListService wishListService;

    @Test
    void 존재하지_않는_상품_ID로_등록_시_404반환() {
        // given
        Member member = new Member(1L, "test@email.com", "test1234", Role.ROLE_USER);
        Long productId = 9999L;

        given(productRepository.findProductByIdOrElseThrow(productId))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 ID의 상품을 찾을 수 없습니다."));

        // when & then
        assertThatThrownBy(() -> wishListService.saveWish(member, productId))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining(HttpStatus.NOT_FOUND.name());
    }

    @Test
    void 존재하지_않는_상품_ID_삭제_시_404반환() {
        Long memberId = 1L;
        Long productId = 9999L;

        assertThatThrownBy(() -> wishListService.deleteWish(memberId, productId))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining(HttpStatus.NOT_FOUND.name());
    }
}
