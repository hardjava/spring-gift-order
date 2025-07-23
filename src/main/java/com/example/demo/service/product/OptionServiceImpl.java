package com.example.demo.service.product;

import com.example.demo.dto.product.ProductOptionRequestDto;
import com.example.demo.dto.product.ProductOptionResponseDto;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductOption;
import com.example.demo.exception.DuplicateOptionException;
import com.example.demo.exception.OptionNotFoundException;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.repository.OptionRepository;
import com.example.demo.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OptionServiceImpl implements OptionService{

  private final OptionRepository optionRepository;
  private final ProductRepository productRepository;

  public OptionServiceImpl(OptionRepository optionRepository, ProductRepository productRepository) {
    this.optionRepository = optionRepository;
    this.productRepository = productRepository;
  }

  public List<ProductOptionResponseDto> getOptions(Long productId){

    List<ProductOption> options = optionRepository.findByProductId(productId);
    if(options.isEmpty()){
      throw new OptionNotFoundException("옵션은 1개 이상 존재해야 합니다.");
    }
    return options.stream()
        .map(ProductOptionResponseDto::from)
        .toList();
  }

  @Transactional
  public void subtractQuantity(Long optionId, int amount){
    ProductOption option = optionRepository.findById(optionId)
        .orElseThrow(() -> new IllegalArgumentException("해당 옵션이 존재하지 않습니다."));

    option.subtract(amount);
  }

  @Transactional
  public ProductOptionResponseDto saveOption(Long productId, ProductOptionRequestDto dto){

    Product product = productRepository.findById(productId)
                                       .orElseThrow(() -> new ProductNotFoundException("상품이 존재하지 않습니다."));

    if (optionRepository.existsByProductAndOptionName(product, dto.name())) {
      throw new DuplicateOptionException("옵션 이름이 중복되었습니다.");
    }

    ProductOption option = new ProductOption(product, dto.name(), dto.quantity());
    ProductOption addOption = optionRepository.save(option);
    return ProductOptionResponseDto.from(addOption);
  }
}
