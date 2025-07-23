package gift.Service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import gift.dto.CreateOptionRequestDto;
import gift.dto.PurchaseOptionRequestDto;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.repository.OptionRepository;
import gift.service.OptionService;
import gift.service.OptionServiceImpl;
import gift.service.ProductService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.config.ConfigData.Options;

@ExtendWith(MockitoExtension.class)
public class OptionServiceTest {

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OptionServiceImpl optionService;

    @Test
    @DisplayName("옵션 이름 중복 시 실패 테스트")
    void 옵션_이름_중복() {
        Long productId = 1L;
        CreateOptionRequestDto request = new CreateOptionRequestDto("옵션 1", 1L);
        List<Option> options = new ArrayList<>();
        Product product = new Product(1L, "상품 1", 123L, "asd", options);
        Option option = new Option(1L, "옵션 1", 1L, product);
        product.addOption(option);
        given(optionRepository.findByProduct_IdAndName(1L, "옵션 1")).
                willReturn(Optional.of(option));
        CustomException e = Assertions.assertThrows(CustomException.class,
                () -> optionService.createOption(request, productId));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.AlreadyExistOptionName);
    }

    @Test
    @DisplayName("현재 수량을 초과하는 수량을 구매 시도 시 실패 테스트")
    void 현재_수량을_초과하는_수량을_구매할_수_없다() {
        Long productId = 1L;
        CreateOptionRequestDto request = new CreateOptionRequestDto("옵션 1", 10L);
        List<Option> options = new ArrayList<>();
        Product product = new Product(1L, "상품 1", 123L, "asd", options);
        Option option = new Option(1L, "옵션 1", 10L, product);
        product.addOption(option);

        PurchaseOptionRequestDto requestDto = new PurchaseOptionRequestDto(11L);

        given(productService.findProductByIdOrElseThrow(productId)).
                willReturn(product);
        given(optionRepository.save(any())).willReturn(option);

        optionService.createOption(request, productId);

        CustomException e = Assertions.assertThrows(CustomException.class,
                () -> optionService.purchaseOption(productId, option.getId(), requestDto));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.OptionNotFound);
    }
}
