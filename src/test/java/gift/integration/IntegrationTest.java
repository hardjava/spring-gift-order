package gift.integration;

import gift.domain.*;
import gift.dto.CreateProductRequestDto;
import gift.dto.OptionRequestDto;
import gift.dto.OrderRequestDto;
import gift.enums.Role;
import gift.repository.*;
import gift.service.MemberService;
import gift.service.OptionService;
import gift.service.OrderService;
import gift.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
public class IntegrationTest {
    @Autowired
    private OptionService optionService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OauthTokenRepository oauthTokenRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private WishListRepository wishListRepository;

    @Test
    void 재고를_정상적으로_차감하면_DB에도_반영된다() {
        // given
        Option option = new Option("[option 1] test", 10);
        Product product = new Product("test", 1200L, "img");
        product.addOption(option);

        Product saved = productRepository.save(product);
        productRepository.flush();

        // when
        optionService.subtract(option.getId(), 5);

        // then
        Option findOption = optionRepository.findByIdOrElseThrow(option.getId());
        assertThat(findOption.getQuantity()).isEqualTo(5);
    }

    @Test
    void 옵션_없이_상품_저장_시_오류를_반환한다() {
        List<OptionRequestDto> optionRequestDtos = new ArrayList<>();
        CreateProductRequestDto requestDto = new CreateProductRequestDto("test", 1200L, "test", optionRequestDtos);
        assertThatThrownBy(() -> productService.createProduct(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("옵션");
    }

    @Test
    void 상품이_삭제되면_옵션도_같이_삭제된다() {
        // given
        Option option1 = new Option("[option 1] test", 10);
        Option option2 = new Option("[option 2] test", 5);
        Product product = new Product("test", 1200L, "img");
        product.addOption(option1);
        product.addOption(option2);

        Product saved = productRepository.save(product);
        productRepository.flush();

        Long productId = saved.getId();
        Long optionId1 = option1.getId();
        Long optionId2 = option2.getId();

        // when
        productRepository.deleteById(productId);

        // then
        assertThat(optionRepository.findById(optionId1)).isEmpty();
        assertThat(optionRepository.findById(optionId2)).isEmpty();
    }

    @Test
    void 로그아웃하면_access_토큰이_삭제된다() {
        // given
        Member member = new Member("test@email.com", "1234", Role.ROLE_USER);
        memberRepository.save(member);
        oauthTokenRepository.save(new OauthToken(member, "access", "refresh"));
        oauthTokenRepository.flush();

        // when
        memberService.logout(member);

        // then
        assertThat(oauthTokenRepository.findByMember(member)).isEmpty();
    }

    @Test
    void 상품_옵션과_해당_수량을_선택하여_주문하면_해당_상품_옵션의_수량이_차감된다() {
        // given
        Product product = new Product("test", 1000L, "test");
        productRepository.save(product);
        productRepository.flush();

        Option option = new Option("Test", 10);
        option.setProduct(product);
        optionRepository.save(option);
        optionRepository.flush();

        Member member = new Member("test@email.com", "1234", Role.ROLE_USER);
        memberRepository.save(member);
        memberRepository.flush();

        OrderRequestDto requestDto = new OrderRequestDto(option.getId(), 5, "test");

        // when
        orderService.order(member, requestDto);

        // then
        assertThat(option.getQuantity()).isEqualTo(5);
    }

    @Test
    void 해당_상품이_위시_리스트에_있는_경우_위시_리스트에서_삭제한다() {
        // given
        Product product = new Product("test", 1000L, "test");
        productRepository.save(product);
        productRepository.flush();

        Option option = new Option("Test", 10);
        option.setProduct(product);
        optionRepository.save(option);
        optionRepository.flush();

        Member member = new Member("test@email.com", "1234", Role.ROLE_USER);
        memberRepository.save(member);
        memberRepository.flush();

        Wish wish = new Wish(member, product);
        wishListRepository.save(wish);
        wishListRepository.flush();

        OrderRequestDto requestDto = new OrderRequestDto(option.getId(), 5, "test");

        // when
        orderService.order(member, requestDto);

        // then
        assertThat(wishListRepository.existsWishByMemberIdAndProductId(member.getId(), product.getId())).isFalse();
    }
}
