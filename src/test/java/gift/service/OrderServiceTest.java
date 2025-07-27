package gift.service;

import gift.domain.Member;
import gift.domain.Option;
import gift.dto.OrderRequestDto;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishListRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OptionRepository optionRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private WishListRepository wishListRepository;

    @Mock
    private KakaoService kakaoService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void 주문할_상품이_존재하지_않을_경우_예외를_던진다() {
        // given
        given(optionRepository.findByIdOrElseThrow(any()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 옵션을 찾을 수 없습니다."));

        OrderRequestDto orderRequestDto = new OrderRequestDto(1L, 1, "test");

        // when & then
        assertThatThrownBy(() -> orderService.order(any(), orderRequestDto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining(HttpStatus.NOT_FOUND.name());
    }

    @Test
    void 주문_수량이_재고보다_많을_경우_예외를_던진다() {
        Option mockOption = org.mockito.Mockito.mock(Option.class);
        Member member = org.mockito.Mockito.mock(Member.class);

        given(optionRepository.findByIdOrElseThrow(any())).willReturn(mockOption);
        doThrow(new IllegalArgumentException("수량이 부족합니다."))
                .when(mockOption).subtract(10);

        OrderRequestDto requestDto = new OrderRequestDto(1L, 10, "테스트 메시지");

        // when & then
        assertThatThrownBy(() -> orderService.order(member, requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("수량");
    }
}
