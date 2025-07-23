package gift.service;

import gift.dto.CreateOptionRequestDto;
import gift.dto.OptionResponseDto;
import gift.dto.PurchaseOptionRequestDto;
import gift.dto.UpdateOptionQuantityRequestDto;
import gift.entity.Option;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface OptionService {

    List<OptionResponseDto> findProductOptionById(Long id);

    OptionResponseDto createOption(CreateOptionRequestDto requestDto, Long id);

    OptionResponseDto setOptionQuantity(
            Long id,
            Long optionId,
            UpdateOptionQuantityRequestDto requestDto);

    OptionResponseDto purchaseOption(
            Long id,
            Long optionId,
            PurchaseOptionRequestDto requestDto);

    void deleteOption(Long id, Long optionId);
}
