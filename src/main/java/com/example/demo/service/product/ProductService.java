package com.example.demo.service.product;

import com.example.demo.dto.product.ProductRequestDto;
import com.example.demo.dto.product.ProductResponseDto;
import com.example.demo.dto.product.ProductUpdateDto;
import com.example.demo.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ProductService {

  ProductResponseDto saveProduct(ProductRequestDto dto);
  ProductResponseDto productFindById(Long id);
  List<ProductResponseDto> productFindAll();
  ProductResponseDto productUpdateById(Long id, ProductUpdateDto dto);
  void productDeleteById(Long id);
  Page<Product> getList(int page);
}
