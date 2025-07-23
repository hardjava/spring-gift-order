package com.example.demo.service.product;

import com.example.demo.dto.product.ProductRequestDto;
import com.example.demo.dto.product.ProductResponseDto;
import com.example.demo.dto.product.ProductUpdateDto;
import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  public ProductServiceImpl(ProductRepository productRepository){
    this.productRepository = productRepository;
  }

  @Override
  @Transactional
  public ProductResponseDto saveProduct(ProductRequestDto dto) {
    Product product = new Product(
        dto.getName(),
        dto.getPrice(),
        dto.getImageUrl()
    );
    Product addProduct = productRepository.save(product);
    return toDto(addProduct);
  }

  @Override
  public ProductResponseDto productFindById(Long id) {
    Product product = productRepository.findById(id)
                                       .orElseThrow(()-> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
    return toDto(product);
  }

  @Override
  public List<ProductResponseDto> productFindAll() {
    return productRepository.findAll()
                            .stream()
                            .map(this::toDto)
                            .toList();
  }

  @Override
  @Transactional
  public ProductResponseDto productUpdateById(Long id, ProductUpdateDto dto) {
    Product product = productRepository.findById(id)
                                       .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품이 존재하지 않습니다: " + id));

    product.updateProductName(dto.getName());
    product.updateProductPrice(dto.getPrice());
    product.updateImageUrl(dto.getImageUrl());
    return toDto(product);
  }

  @Override
  @Transactional
  public void productDeleteById(Long id) {
    productRepository.deleteById(id);
  }

  @Override
  public Page<Product> getList(int page) {
    Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
    return this.productRepository.findAll(pageable);
  }

  private ProductResponseDto toDto(Product product){
    return new ProductResponseDto(
        product.getId(),
        product.getName(),
        product.getPrice(),
        product.getImageUrl()
    );
  }
}
