package gift.wish.service;

import gift.member.entity.Member;
import gift.member.entity.Role;
import gift.member.repository.MemberRepository;
import gift.product.entity.Product;
import gift.product.exception.ProductNotFoundException;
import gift.product.repository.ProductRepository;
import gift.wish.dto.WishResponseDto;
import gift.wish.entity.Wish;
import gift.wish.repository.WishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class WishServiceTest {

    private WishRepository wishRepository;
    private ProductRepository productRepository;
    private MemberRepository memberRepository;
    private WishServiceImpl wishService;

    @BeforeEach
    void setUp() {
        wishRepository = mock(WishRepository.class);
        productRepository = mock(ProductRepository.class);
        memberRepository = mock(MemberRepository.class);

        wishService = new WishServiceImpl(wishRepository, productRepository, memberRepository);
    }

    @Test
    @DisplayName("상품이 존재하면, 위시를 추가할 수 있다. ")
    void shouldCreateWish() {
        Long memberId = 1L;
        Long productId = 2L;

        Member member = new Member(memberId, "솨야", "park@gmail.com", "pw", Role.USER);
        Product product = new Product(productId, "하리보 젤리", 1500, "http://img.url/test.png");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(wishRepository.existsByMemberIdAndProductId(memberId, productId)).thenReturn(false);
        when(wishRepository.save(any(Wish.class))).thenReturn(new Wish(member, product));

        WishResponseDto result = wishService.createWish(memberId, productId);

        assertThat(result.productId()).isEqualTo(productId);
        assertThat(result.productName()).isEqualTo("하리보 젤리");
    }

    @Test
    @DisplayName("상품이 존재하지 않으면, 404(Not Found) 예외가 발생한다. ")
    void shouldThrowIfProductNotFound() {
        Long memberId = 1L;
        Member member = new Member(memberId, "솨야", "park@gmail.com", "1234", Role.USER);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> wishService.createWish(1L, 999L))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("위시 ID로 삭제 시, deleteWishById가 수행된다. ")
    void shouldDeleteWish() {
        Long memberId = 1L;
        Long productId = 2L;
        Long wishId = 10L;

        Member  member  = new Member(memberId,  "솨야", "park@gmail.com", "pw", Role.USER);
        Product product = new Product(productId, "하리보 젤리", 1500, "http://img.url/test.png");
        Wish    wish    = new Wish(member, product);

        ReflectionTestUtils.setField(wish, "id", wishId);

        when(wishRepository.findById(wishId))
                .thenReturn(Optional.of(wish));

        wishService.deleteWish(memberId, wishId);

        verify(wishRepository, atLeastOnce()).delete(wish);
    }

    @Test
    @DisplayName("회원 ID로 모든 위시를 조회 시, 위시 목록을 반환한다. ")
    void shouldReturnAllWishesByMember() {
        Long memberId = 1L;

        Member member = new Member(memberId, "솨야", "park@gmail.com", "1234", Role.USER);
        Product product = new Product(2L, "하리보 젤리(콜라맛)", 2000, "http://img.url/coke.png");
        Wish wish = new Wish(member, product);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Wish> wishPage = new PageImpl<>(List.of(wish), pageable, 1);

        when(wishRepository.findAllByMemberId(memberId, pageable)).thenReturn(wishPage);
        when(productRepository.findById(2L)).thenReturn(Optional.of(product));

        Page<WishResponseDto> result = wishService.findAllWishesByMemberId(memberId, pageable);

        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0).productName()).isEqualTo("하리보 젤리(콜라맛)");
    }
}
