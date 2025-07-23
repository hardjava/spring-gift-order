package com.example.demo.service.product;

import com.example.demo.dto.product.ProductOptionRequestDto;
import com.example.demo.dto.product.ProductOptionResponseDto;
import java.util.List;

public interface OptionService {

  List<ProductOptionResponseDto> getOptions(Long productId);
  ProductOptionResponseDto saveOption(Long productId, ProductOptionRequestDto dto);
}
