package gift.integration;

import gift.domain.Option;
import gift.domain.Product;
import gift.dto.CreateProductRequestDto;
import gift.dto.OptionRequestDto;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.service.OptionService;
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
    private OptionRepository optionRepository;

    @Autowired
    private ProductRepository productRepository;

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
}
