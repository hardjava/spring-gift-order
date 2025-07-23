package gift.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class WishRepositoryTest {

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;


    @Test
    @DisplayName("위시 저장 테스트")
    void save() {
        Product product = new Product("아이스아메리카노", 1500L, "asd.dsa");
        Member member = new Member(1L, "asd@asd.asd", "dasdada", "user");
        Wish expected = new Wish(product, member, 4L);
        memberRepository.save(member);
        productRepository.save(product);
        wishRepository.save(expected);
        Long id = expected.getId();
        Wish actual = wishRepository.findById(id).get();
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getProduct()).isEqualTo(expected.getProduct()),
                () -> assertThat(actual.getMember()).isEqualTo(expected.getMember()),
                () -> assertThat(actual.getQuantity()).isEqualTo(expected.getQuantity())
        );
    }

    @Test
    @DisplayName("상품 아이디에 해당하는 위시 조회 테스트")
    void findByProduct_IdAndMember_Id() {
        Product product = new Product("아이스아메리카노", 1500L, "asd.dsa");
        Member member = new Member("asd@asd.asd", "dasdada", "user");

        memberRepository.save(member);
        productRepository.save(product);

        Wish expected = new Wish(product, member, 4L);
        wishRepository.save(expected);

        Wish actual = wishRepository.findByProduct_IdAndMember_Id(product.getId(), member.getId())
                .get();
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getProduct()).isEqualTo(expected.getProduct()),
                () -> assertThat(actual.getMember()).isEqualTo(expected.getMember()),
                () -> assertThat(actual.getQuantity()).isEqualTo(expected.getQuantity())
        );

    }

    @Test
    @DisplayName("상품 아이디에 해당하는 위시 삭제 테스트")
    void deleteByProduct_IdAndMember_Id() {
        Product product = new Product("아이스아메리카노", 1500L, "asd.dsa");
        Member member = new Member("asd@asd.asd", "dasdada", "user");

        memberRepository.save(member);
        productRepository.save(product);

        Wish expected = new Wish(product, member, 4L);

        wishRepository.save(expected);

        wishRepository.deleteByProduct_IdAndMember_Id(1L, 1L);

        Optional<Wish> actual = wishRepository.findByProduct_IdAndMember_Id(product.getId(),
                member.getId());
        assertAll(
                () -> assertThat(actual.isEmpty())
        );
    }

}
