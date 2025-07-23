package gift.service;

import gift.domain.Option;
import gift.repository.OptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class OptionServiceTest {
    @Mock
    private OptionRepository optionRepository;

    @InjectMocks
    private OptionService optionService;

    @Test
    void 재고보다_많은_수량을_차감하면_예외를_던진다() {
        // given
        Option option = new Option("[test] option1", 10);

        // when
        given(optionRepository.findByIdOrElseThrow(anyLong()))
                .willReturn(option);

        // then
        assertThatThrownBy(() -> optionService.subtract(anyLong(), 20))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("재고");
    }

    @Test
    void 정상_수량을_차감하면_예외없이_동작한다() {
        Option option = new Option("[test] option1", 10);

        given(optionRepository.findByIdOrElseThrow(anyLong()))
                .willReturn(option);

        optionService.subtract(anyLong(), 3);

        assertThat(option.getQuantity()).isEqualTo(7);
    }
}
