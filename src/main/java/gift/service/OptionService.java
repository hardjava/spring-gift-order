package gift.service;

import gift.domain.Option;
import gift.domain.OptionResponse;
import gift.dto.OptionResponseDto;
import gift.repository.OptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OptionService {
    private final OptionRepository optionRepository;

    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    public OptionResponseDto getAllOptions(Long productID) {
        List<Option> findOptions = optionRepository.findByProductIdOrElseThrow(productID);

        return new OptionResponseDto(
                findOptions.stream()
                        .map(OptionResponse::from)
                        .toList()
        );
    }

    @Transactional
    public void subtract(Long optionId, int quantity) {
        Option findOption = optionRepository.findByIdOrElseThrow(optionId);
        findOption.subtract(quantity);
    }
}
