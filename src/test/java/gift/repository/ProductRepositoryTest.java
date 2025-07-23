package gift.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품 아이디로 삭제 테스트")
    void deleteById() {
        Product expected = new Product("아이스아메리카노", 1500L, "asd.dsa");
        Product actual = productRepository.save(expected);

        productRepository.deleteById(actual.getId());
        assertAll(
                () -> assertThat(productRepository.findById(actual.getId())).isEmpty()
        );
    }

    @Test
    @DisplayName("상품을 아이디로 조회 테스트")
    void findById() {
        Product expected = new Product("아이스아메리카노", 1500L, "asd.dsa");
        productRepository.save(expected);
        Long id = expected.getId();
        Product actual = productRepository.findById(id).get();
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice()),
                () -> assertThat(actual.getImageUrl()).isEqualTo(expected.getImageUrl())
        );
    }

    @Test
    @DisplayName("상품 등록 테스트")
    void save() {
        Product expected = new Product("아이스아메리카노", 1500L, "asd.dsa");
        productRepository.save(expected);
        Long id = expected.getId();
        Product actual = productRepository.findById(id).get();
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice()),
                () -> assertThat(actual.getImageUrl()).isEqualTo(expected.getImageUrl())
        );
    }
}