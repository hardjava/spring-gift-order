package gift.service;

import gift.dto.OptionResponseDto;
import gift.entity.Option;
import gift.entity.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class OptionServiceTest {

    private OptionService optionService;
    private OptionRepository optionRepository;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        optionRepository = Mockito.mock(OptionRepository.class);
        productRepository = Mockito.mock(ProductRepository.class);
        optionService = new OptionService(optionRepository, productRepository);
    }

    @Test
    void getOptionsByProductId_Success() {
        Long productId = 1L;
        given(productRepository.existsById(productId)).willReturn(true);
        Option option = new Option(new Product(productId, "p", 100, "url"), "opt1", 5);
        given(optionRepository.findByProductId(productId)).willReturn(List.of(option));

        List<OptionResponseDto> result = optionService.getOptionsByProductId(productId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("opt1");
    }

    @Test
    void getOptionsByProductId_ProductNotFound() {
        given(productRepository.existsById(1L)).willReturn(false);
        assertThatThrownBy(() -> optionService.getOptionsByProductId(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("상품이 존재하지 않습니다.");
    }

    @Test
    void getOptionByIdAndProductId_Success() {
        Long productId = 1L, optionId = 2L;
        Option option = new Option(new Product(productId, "p", 100, "url"), "opt", 10);
        given(optionRepository.findByIdAndProductId(optionId, productId))
                .willReturn(Optional.of(option));

        OptionResponseDto dto = optionService.getOptionByIdAndProductId(optionId, productId);

        assertThat(dto.id()).isEqualTo(optionId);
        assertThat(dto.name()).isEqualTo("opt");
        assertThat(dto.quantity()).isEqualTo(10);
    }

    @Test
    void getOptionByIdAndProductId_NotFound() {

        given(optionRepository.findByIdAndProductId(2L, 1L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> optionService.getOptionByIdAndProductId(2L, 1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("옵션을 찾을 수 없습니다.");
    }

    @Test
    void addOption_Success() {

        Long productId = 1L;
        String name = "opt";
        int quantity = 5;
        Product product = new Product(productId, "p", 100, "url");
        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(optionRepository.existsByProductIdAndName(productId, name)).willReturn(false);

        optionService.addOption(productId, name, quantity);

        assertThat(product.getOptions()).hasSize(1);
        Option added = product.getOptions().get(0);
        assertThat(added.getName()).isEqualTo(name);
        assertThat(added.getQuantity()).isEqualTo(quantity);
    }

    @Test
    void addOption_ProductNotFound() {
        given(productRepository.findById(1L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> optionService.addOption(1L, "opt", 5))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("상품이 존재하지 않습니다.");
    }

    @Test
    void addOption_DuplicateName() {
        Long productId = 1L;
        given(productRepository.findById(productId))
                .willReturn(Optional.of(new Product(productId, "p", 100, "url")));
        given(optionRepository.existsByProductIdAndName(productId, "opt")).willReturn(true);

        assertThatThrownBy(() -> optionService.addOption(productId, "opt", 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 옵션 이름입니다.");
    }

    @Test
    void subtractQuantity_Success() {
        Option option = new Option(new Product(1L, "p", 100, "url"), "opt", 10);
        given(optionRepository.findById(1L)).willReturn(Optional.of(option));

        optionService.subtractQuantity(1L, 3);
        assertThat(option.getQuantity()).isEqualTo(7);
    }

    @Test
    void subtractQuantity_OptionNotFound() {
        given(optionRepository.findById(1L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> optionService.subtractQuantity(1L, 1))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("옵션을 찾을 수 없습니다.");
    }

    @Test
    void subtractQuantity_InsufficientStock() {
        Option option = new Option(new Product(1L, "p", 100, "url"), "opt", 2);
        given(optionRepository.findById(1L)).willReturn(Optional.of(option));
        assertThatThrownBy(() -> optionService.subtractQuantity(1L, 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족합니다.");
    }

    @Test
    void updateOption_Success() {
        Long productId = 1L, optionId = 2L;
        Product product = new Product(productId, "p", 100, "url");
        Option option = new Option(product, "old", 5);
        option.setId(optionId);
        product.addOption(option);
        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        optionService.updateOption(productId, optionId, "new", 10);
        assertThat(option.getName()).isEqualTo("new");
        assertThat(option.getQuantity()).isEqualTo(10);
    }

    @Test
    void updateOption_ProductNotFound() {
        given(productRepository.findById(1L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> optionService.updateOption(1L, 2L, "n", 5))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("상품이 존재하지 않습니다.");
    }

    @Test
    void updateOption_OptionNotFound() {
        Long productId = 1L, optionId = 2L;
        Product product = new Product(productId, "p", 100, "url");
        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        assertThatThrownBy(() -> optionService.updateOption(productId, optionId, "n", 5))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("옵션을 찾을 수 없습니다.");
    }

    @Test
    void updateOption_DuplicateName() {
        Long productId = 1L, optionId = 2L;
        Product product = new Product(productId, "p", 100, "url");
        Option option1 = new Option(product, "old", 5);
        option1.setId(optionId);
        Option option2 = new Option(product, "dup", 7);
        option2.setId(3L);
        product.addOption(option1);
        product.addOption(option2);
        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        assertThatThrownBy(() -> optionService.updateOption(productId, optionId, "dup", 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 옵션 이름입니다.");
    }

    @Test
    void deleteOption_Success() {
        Long productId = 1L, optionId = 2L;
        Product product = new Product(productId, "p", 100, "url");
        Option option = new Option(product, "opt", 5);
        option.setId(optionId);
        product.addOption(option);
        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        optionService.deleteOption(productId, optionId);
        assertThat(product.getOptions()).doesNotContain(option);
    }

    @Test
    void deleteOption_ProductNotFound() {
        given(productRepository.findById(1L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> optionService.deleteOption(1L, 2L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("상품이 존재하지 않습니다.");
    }

    @Test
    void deleteOption_OptionNotFound() {
        Long productId = 1L, optionId = 2L;
        Product product = new Product(productId, "p", 100, "url");
        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        assertThatThrownBy(() -> optionService.deleteOption(productId, optionId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("옵션을 찾을 수 없습니다.");
    }
}
