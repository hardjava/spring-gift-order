package gift.option.service;

import gift.option.dto.OptionCreateRequestDto;
import gift.option.dto.OptionResponseDto;
import gift.option.dto.OptionUpdateRequestDto;
import gift.option.entity.Option;
import gift.option.exception.DuplicateOptionException;
import gift.option.exception.OptionNotFoundException;
import gift.option.exception.OptionRequiredException;
import gift.option.repository.OptionRepository;
import gift.product.entity.Product;
import gift.product.exception.ProductNotFoundException;
import gift.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OptionServiceImpl implements OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionServiceImpl(OptionRepository optionRepository,
                             ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptionResponseDto> getOptions(Long productId) {
        return optionRepository.findByProductId(productId).stream()
                .map(OptionResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public OptionResponseDto createOption(Long productId, OptionCreateRequestDto request) {
        if (optionRepository.existsByProductIdAndName(productId, request.name())) {
            throw new DuplicateOptionException();
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Option option = new Option(product, request.name(), request.quantity());
        optionRepository.save(option);

        return OptionResponseDto.from(option);
    }

    @Override
    @Transactional
    public void subtractQuantity(Long optionId, int quantity) {
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new OptionNotFoundException(optionId));

        option.subtract(quantity);
    }

    @Override
    @Transactional
    public OptionResponseDto updateOption(Long productId, Long optionId, OptionUpdateRequestDto request) {
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new OptionNotFoundException(optionId));

        if (!option.getName().equals(request.name())
                && optionRepository.existsByProductIdAndName(productId, request.name())) {
            throw new DuplicateOptionException();
        }

        option.update(request.name(), request.quantity());
        return OptionResponseDto.from(option);
    }

    @Override
    @Transactional
    public void deleteOption(Long productId, Long optionId) {
        List<Option> options = optionRepository.findByProductId(productId);

        if (options.size() <= 1) {
            throw new OptionRequiredException();
        }

        optionRepository.deleteById(optionId);
    }
}
