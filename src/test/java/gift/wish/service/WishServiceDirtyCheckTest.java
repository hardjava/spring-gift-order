package gift.wish.service;

import gift.member.entity.Member;
import gift.member.entity.Role;
import gift.member.repository.MemberRepository;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import gift.wish.entity.Wish;
import gift.wish.repository.WishRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(properties = {
        "spring.sql.init.mode=never",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
class WishServiceDirtyCheckTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("더티체킹으로 Wish의 product를 변경하면 트랜잭션 커밋 시 DB에 반영된다.")
    void dirtyCheck_updatesWishProduct() {
        Member member = memberRepository.save(new Member("솨야", "psh@test.com", "1234", Role.USER));
        Product product = productRepository.save(new Product("하리보 젤리(original)", 1500, "http://img.url/original.png"));
        Product replacement = productRepository.save(new Product("하리보 젤리(new)", 2000, "http://img.url/new.png"));

        Wish wish = wishRepository.save(new Wish(member, product));
        entityManager.flush();
        entityManager.clear();

        Wish managed = entityManager.find(Wish.class, wish.getId());
        ReflectionTestUtils.setField(managed, "product", replacement);

        entityManager.flush();
        entityManager.clear();

        Wish fromDb = wishRepository.findById(wish.getId()).orElseThrow();
        assertThat(fromDb.getProduct().getId()).isEqualTo(replacement.getId());
        assertThat(fromDb.getProduct().getName()).isEqualTo(replacement.getName());
        assertThat(fromDb.getProduct().getPrice()).isEqualTo(replacement.getPrice());
        assertThat(fromDb.getProduct().getImageUrl()).isEqualTo(replacement.getImageUrl());
    }
}