package gift.option.repository;

import gift.option.entity.Option;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OptionRepositoryTest {

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품 ID로 옵션 목록을 조회할 수 있다. ")
    void findByProductId_returnsList() {
        Product p = new Product("하리보 젤리", 2000, "http://img.url/test.png");
        productRepository.save(p);
        optionRepository.save(new Option(p, "낱개", 10));
        optionRepository.save(new Option(p, "묶음", 5));

        var list = optionRepository.findByProductId(p.getId());
        assertThat(list).hasSize(2);
    }

    @Test
    @DisplayName("동일 상품 내 옵션명 중복 여부를 반환한다. ")
    void existsByProductIdAndName_returnsTrue() {
        Product p = new Product("하리보 젤리", 2000, "http://img.url/test.png");
        productRepository.save(p);
        optionRepository.save(new Option(p, "낱개", 10));

        boolean exists = optionRepository.existsByProductIdAndName(p.getId(), "낱개");
        assertThat(exists).isTrue();
    }
}
