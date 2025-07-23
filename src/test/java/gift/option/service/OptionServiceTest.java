package gift.option.service;

import gift.option.dto.OptionCreateRequestDto;
import gift.option.dto.OptionUpdateRequestDto;
import gift.option.entity.Option;
import gift.option.exception.DuplicateOptionException;
import gift.option.exception.OptionNotFoundException;
import gift.option.exception.OptionRequiredException;
import gift.option.repository.OptionRepository;
import gift.product.entity.Product;
import gift.product.exception.ProductNotFoundException;
import gift.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class OptionServiceTest {

    private OptionRepository optionRepository;
    private ProductRepository productRepository;
    private OptionService optionService;

    @BeforeEach
    void setUp() {
        optionRepository = mock(OptionRepository.class);
        productRepository = mock(ProductRepository.class);
        optionService = new OptionServiceImpl(optionRepository, productRepository);
    }

    @Test
    @DisplayName("상품 옵션을 생성할 수 있다. ")
    void createOption_success() {
        Long productId = 1L;
        var req = new OptionCreateRequestDto("낱개", 10);
        var product = new Product("하리보 젤리", 1500, "http://img.url/test.png");

        when(optionRepository.existsByProductIdAndName(productId, "낱개")).thenReturn(false);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        optionService.createOption(productId, req);

        verify(optionRepository).save(any(Option.class));
    }

    @Test
    @DisplayName("동일 상품 내에 옵션명이 중복되면 예외가 발생한다. ")
    void createOption_duplicateName_throws() {
        Long productId = 1L;
        var req = new OptionCreateRequestDto("낱개", 10);

        when(optionRepository.existsByProductIdAndName(productId, "낱개")).thenReturn(true);

        assertThatThrownBy(() -> optionService.createOption(productId, req))
                .isInstanceOf(DuplicateOptionException.class)
                .hasMessage("같은 상품 내에 동일한 옵션명이 존재합니다.");
    }

    @Test
    @DisplayName("존재하지 않는 상품에 옵션 추가 시 예외가 발생한다. ")
    void createOption_productNotFound_throws() {
        Long productId = 1L;
        var req = new OptionCreateRequestDto("낱개", 10);

        when(optionRepository.existsByProductIdAndName(productId, "낱개")).thenReturn(false);
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> optionService.createOption(productId, req))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("id=" + productId);
    }

    @Test
    @DisplayName("옵션 수량을 지정된 수만큼 차감할 수 있다. ")
    void subtractQuantity_success() {
        Long optionId = 1L;
        Option option = mock(Option.class);

        when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));

        optionService.subtractQuantity(optionId, 5);

        verify(option).subtract(5);
    }

    @Test
    @DisplayName("존재하지 않는 옵션에 수량 차감 시 예외가 발생한다. ")
    void subtractQuantity_notFound_throws() {
        when(optionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> optionService.subtractQuantity(999L, 1))
                .isInstanceOf(OptionNotFoundException.class)
                .hasMessageContaining("id=999");
    }

    @Test
    @DisplayName("옵션을 수정할 수 있다. ")
    void updateOption_success() {
        Long productId = 1L, optionId = 1L;
        var req = new OptionUpdateRequestDto("묶음", 20);
        var product = new Product("하리보 젤리", 1500, "http://img.url/test.png");
        Option option = new Option(product, "낱개", 10);

        when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));
        when(optionRepository.existsByProductIdAndName(productId, "묶음")).thenReturn(false);

        optionService.updateOption(productId, optionId, req);

        assertThat(option.getName()).isEqualTo("묶음");
        assertThat(option.getQuantity()).isEqualTo(20);
    }

    @Test
    @DisplayName("옵션 수정 시 중복된 이름을 입력하면 예외가 발생한다. ")
    void updateOption_duplicateName_throws() {
        Long productId = 1L, optionId = 1L;
        var req = new OptionUpdateRequestDto("낱개", 20);
        var product = new Product("하리보 젤리", 1500, "http://img.url/test.png");
        Option option = new Option(product, "묶음", 10);

        when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));
        when(optionRepository.existsByProductIdAndName(productId, "낱개")).thenReturn(true);

        assertThatThrownBy(() -> optionService.updateOption(productId, optionId, req))
                .isInstanceOf(DuplicateOptionException.class);
    }

    @Test
    @DisplayName("옵션이 없으면 삭제 시 OptionRequiredException(404)이 발생한다. ")
    void deleteOption_noOptions_throws() {
        Long productId = 1L;
        Long optionId  = 10L;

        when(optionRepository.findByProductId(productId))
                .thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> optionService.deleteOption(productId, optionId))
                .isInstanceOf(OptionRequiredException.class)
                .hasMessage("상품에는 하나 이상의 옵션이 필요합니다.");

        verify(optionRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("옵션이 두 개 이상 있으면 정상적으로 deleteById를 호출한다. ")
    void deleteOption_success() {
        Long productId = 1L;
        Long optionId  = 10L;

        when(optionRepository.findByProductId(productId))
                .thenReturn(List.of(
                        mock(Option.class),
                        mock(Option.class)
                ));

        optionService.deleteOption(productId, optionId);

        verify(optionRepository).deleteById(optionId);
    }
}
