package gift.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.entity.Option;
import gift.entity.Product;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class OptionRepositoryTest {

    private final Product product = new Product("아이스티", 2000L, "asd.dsa");
    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("옵션 저장 테스트")
    void save() {
        Product savedProduct = productRepository.save(product);
        Option expected = new Option("샷추가", 5L, product);
        optionRepository.save(expected);

        Optional<Option> optional = optionRepository.findByProduct_IdAndName(savedProduct.getId(),
                expected.getName());
        Option actual = optional.get();
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getQuantity()).isEqualTo(expected.getQuantity())
        );
    }

    @Test
    @DisplayName("상품 아이디에 해당하는 옵션 조회 테스트")
    void findByProduct_Id() {

        Product savedProduct = productRepository.save(product);
        Option expected = new Option("2샷", 99L, product);
        optionRepository.save(expected);

        List<Option> actual = optionRepository.findByProduct_Id(savedProduct.getId());

        assertThat(!actual.isEmpty());

        Option actualOption = actual.get(0);
        assertAll(
                () -> assertThat(actualOption.getId()).isNotNull(),
                () -> assertThat(actualOption.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actualOption.getQuantity()).isEqualTo(expected.getQuantity())
        );
    }

    @Test
    @DisplayName("상품 아이디, 옵션 아이디에 해당하는 옵션 삭제 테스트")
    void deleteById() {
        productRepository.save(product);
        Option expected = new Option("3샷", 101L, product);
        Option actual = optionRepository.save(expected);
        Long optionId = actual.getId();
        optionRepository.deleteById(optionId);

        Optional<Option> deleted = optionRepository.findByProduct_IdAndId(product.getId(),
                optionId);

        assertAll(
                () -> assertThat(deleted.isEmpty())
        );


    }
}
