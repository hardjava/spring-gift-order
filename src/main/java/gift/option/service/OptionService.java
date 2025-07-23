package gift.option.service;

import gift.option.dto.OptionCreateRequestDto;
import gift.option.dto.OptionResponseDto;
import gift.option.dto.OptionUpdateRequestDto;

import java.util.List;

public interface OptionService {
    List<OptionResponseDto> getOptions(Long productId);
    OptionResponseDto createOption(Long productId, OptionCreateRequestDto createRequestDto);
    void subtractQuantity(Long optionId, int quantity);
    OptionResponseDto updateOption(Long productId, Long optionId, OptionUpdateRequestDto updateRequestDto);
    void deleteOption(Long productId, Long optionId);
}
