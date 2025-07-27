package gift.service;

import gift.domain.Member;
import gift.domain.Option;
import gift.domain.Order;
import gift.domain.OrderInfo;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.enums.OauthProvider;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OptionRepository optionRepository;
    private final OrderRepository orderRepository;
    private final WishListRepository wishListRepository;
    private final KakaoService kakaoService;

    public OrderService(OptionRepository optionRepository, OrderRepository orderRepository, WishListRepository wishListRepository, KakaoService kakaoService) {
        this.optionRepository = optionRepository;
        this.orderRepository = orderRepository;
        this.wishListRepository = wishListRepository;
        this.kakaoService = kakaoService;
    }

    @Transactional
    public OrderResponseDto order(Member member, OrderRequestDto requestDto) {
        Option findOption = optionRepository.findByIdOrElseThrow(requestDto.optionId());
        findOption.subtract(requestDto.quantity());
        wishListRepository.deleteWishesByMemberAndProduct(member, findOption.getProduct());
        Order order = new Order(member, findOption, requestDto.quantity(), requestDto.message());
        orderRepository.save(order);

        if (member.getOauthProvider().equals(OauthProvider.PROVIDER_KAKAO)) {
            kakaoService.sendKakaoMessage(OrderInfo.from(order));
        }

        return OrderResponseDto.from(order);
    }
}
