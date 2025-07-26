package gift.service;

import gift.domain.Member;
import gift.domain.Option;
import gift.domain.Order;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final MemberRepository memberRepository;
    private final OptionRepository optionRepository;
    private final OrderRepository orderRepository;
    private final WishListRepository wishListRepository;

    public OrderService(MemberRepository memberRepository, OptionRepository optionRepository, OrderRepository orderRepository, WishListRepository wishListRepository) {
        this.memberRepository = memberRepository;
        this.optionRepository = optionRepository;
        this.orderRepository = orderRepository;
        this.wishListRepository = wishListRepository;
    }

    @Transactional
    public OrderResponseDto order(Member member, OrderRequestDto requestDto) {
        Option findOption = optionRepository.findByIdOrElseThrow(requestDto.optionId());
        findOption.subtract(requestDto.quantity());
        wishListRepository.deleteWishesByMemberAndProduct(member, findOption.getProduct());
        Order order = new Order(member, findOption, requestDto.quantity(), requestDto.message());

        return OrderResponseDto.from(orderRepository.save(order));
    }
}
