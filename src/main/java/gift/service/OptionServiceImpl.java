package gift.service;

import gift.dto.CreateOptionRequestDto;
import gift.dto.OptionResponseDto;
import gift.dto.PurchaseOptionRequestDto;
import gift.dto.UpdateOptionQuantityRequestDto;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.repository.OptionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptionServiceImpl implements OptionService {

    private final OptionRepository optionRepository;

    private final ProductService productService;

    public OptionServiceImpl(OptionRepository optionRepository, ProductService productService) {
        this.optionRepository = optionRepository;
        this.productService = productService;
    }

    @Override
    public List<OptionResponseDto> findProductOptionById(Long id) {
        List<Option> options = findOptionsByProductIdOrElseThrow(id);
        return toOptionResponseDtoList(options);
    }

    @Override
    @Transactional
    public OptionResponseDto createOption(CreateOptionRequestDto requestDto, Long productId) {
        checkDuplicateOption(productId, requestDto.name());
        Option newOption = new Option(requestDto.name(), requestDto.quantity(), null);
        Product product = productService.findProductByIdOrElseThrow(productId);
        newOption.setProduct(product);
        Option savedOption = optionRepository.save(newOption);
        return new OptionResponseDto(savedOption.getId(), savedOption.getName(),
                savedOption.getQuantity());
    }

    @Override
    @Transactional
    public OptionResponseDto setOptionQuantity(
            Long id,
            Long optionId,
            UpdateOptionQuantityRequestDto requestDto) {
        Option option = findOptionByProductIdAndOptionIdOrElseThrow(id, optionId);
        option.changeQuantity(requestDto.quantity());
        Option updatedOption = findOptionByProductIdAndOptionIdOrElseThrow(id, optionId);
        return new OptionResponseDto(updatedOption.getId(), updatedOption.getName(),
                updatedOption.getQuantity());
    }

    @Override
    @Transactional
    public OptionResponseDto purchaseOption(
            Long id,
            Long optionId,
            PurchaseOptionRequestDto requestDto) {
        Option option = findOptionByProductIdAndOptionIdOrElseThrow(id, optionId);
        if (requestDto.quantity() > option.getQuantity()) {
            throw new CustomException(ErrorCode.OptionNotEnough);
        }
        option.changeQuantity(option.getQuantity() - requestDto.quantity());
        Option updatedOption = findOptionByProductIdAndOptionIdOrElseThrow(id, optionId);
        return new OptionResponseDto(updatedOption.getId(), updatedOption.getName(),
                updatedOption.getQuantity());
    }

    @Override
    @Transactional
    public void deleteOption(Long id, Long optionId) {
        findOptionByProductIdAndOptionIdOrElseThrow(id, optionId);
        optionRepository.deleteById(optionId);
    }

    private Option findOptionByProductIdAndOptionIdOrElseThrow(Long productId, Long optionId) {
        return optionRepository.findByProduct_IdAndId(productId, optionId)
                .orElseThrow(() -> new CustomException(ErrorCode.OptionNotFound));
    }

    private void checkDuplicateOption(Long productId, String name) {
        optionRepository.findByProduct_IdAndName(productId, name)
                .ifPresent(option -> {
                    throw new CustomException(ErrorCode.AlreadyExistOptionName);
                });
    }

    private List<OptionResponseDto> toOptionResponseDtoList(List<Option> options) {
        return options
                .stream()
                .map(option -> new OptionResponseDto(option.getId(), option.getName(),
                        option.getQuantity()))
                .collect(Collectors.toList());
    }

    private List<Option> findOptionsByProductIdOrElseThrow(Long productId) {
        List<Option> options = optionRepository.findByProduct_Id(productId);
        if (options.isEmpty()) {
            throw new CustomException(ErrorCode.ProductNotfound);
        }
        return options;
    }
}
