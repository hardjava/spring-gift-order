package gift.product.repository;

import gift.product.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository products;

    @Test
    void save() {
        // given
        Product expected = new Product("하리보 젤리", 1500, "http://img.url/test.png");

        // when
        Product actual = products.save(expected);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo("하리보 젤리"),
                () -> assertThat(actual.getPrice()).isEqualTo(1500),
                () -> assertThat(actual.getImageUrl()).isEqualTo("http://img.url/test.png")
        );
    }

    @Test
    void findById() {
        // given
        Product saved = products.save(new Product("하리보 젤리(콜라맛)", 2000, "http://img.url/coke.png"));

        // when
        Optional<Product> result = products.findById(saved.getId());

        // then
        assertThat(result).isPresent();
        assertAll(
                () -> assertThat(result.get().getName()).isEqualTo("하리보 젤리(콜라맛)"),
                () -> assertThat(result.get().getPrice()).isEqualTo(2000),
                () -> assertThat(result.get().getImageUrl()).isEqualTo("http://img.url/coke.png")
        );
    }

    @Test
    void update() {
        // given
        Product product = products.save(new Product("하리보 젤리(오리지널)", 1500, "http://img.url/original.png"));

        // when
        product.update("하리보 젤리(리뉴얼)", 2000, "http://img.url/new.png");
        Product updated = products.findById(product.getId()).orElseThrow();

        // then
        assertAll(
                () -> assertThat(updated.getName()).isEqualTo("하리보 젤리(리뉴얼)"),
                () -> assertThat(updated.getPrice()).isEqualTo(2000),
                () -> assertThat(updated.getImageUrl()).isEqualTo("http://img.url/new.png")
        );
    }

    @Test
    void delete() {
        // given
        Product saved = products.save(new Product("하리보 젤리", 1500, "http://img.url/test.png"));

        // when
        products.delete(saved);

        // then
        assertThat(products.findById(saved.getId())).isEmpty();
    }
}
