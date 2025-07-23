package gift.wish.repository;

import gift.member.entity.Member;
import gift.member.entity.Role;
import gift.member.repository.MemberRepository;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import gift.wish.entity.Wish;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class WishRepositoryTest {

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    private Member member;
    private Product product;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member("솨야", "park@gmail.com", "1234", Role.USER));
        product = productRepository.save(new Product("하리보 젤리", 1500, "http://img.url/test.png"));
    }

    @Test
    void save() {
        // given
        Wish toSave = new Wish(member, product);

        // when
        Wish saved = wishRepository.save(toSave);

        // then
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getMember()).isEqualTo(member),
                () -> assertThat(saved.getProduct()).isEqualTo(product)
        );
    }

    @Test
    void findById() {
        // given
        Wish saved = wishRepository.save(new Wish(member, product));

        // when
        Optional<Wish> result = wishRepository.findById(saved.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getMember()).isEqualTo(member);
        assertThat(result.get().getProduct()).isEqualTo(product);
    }

    @Test
    void findAllByMemberId() {
        // given
        for (long i = 1; i <= 20; i++) {
            Product newProduct = productRepository.save(new Product("상품" + i, (int) (1000 + i), "http://img/" + i));
            wishRepository.save(new Wish(member, newProduct));
        }

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Wish> result = wishRepository.findAllByMemberId(member.getId(), pageable);

        // then
        assertThat(result.getContent()).hasSize(10);
        assertThat(result.getTotalElements()).isEqualTo(20);
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.getNumber()).isEqualTo(0);
    }

    @Test
    void existsByMemberIdAndProductId() {
        // given
        wishRepository.save(new Wish(member, product));

        // then
        assertThat(wishRepository.existsByMemberIdAndProductId(member.getId(), product.getId())).isTrue();
        assertThat(wishRepository.existsByMemberIdAndProductId(member.getId(), 999L)).isFalse();
    }

    @Test
    void delete() {
        // given
        Wish saved = wishRepository.save(new Wish(member, product));

        // when
        wishRepository.delete(saved);

        // then
        assertThat(wishRepository.findById(saved.getId())).isEmpty();
    }
}
