package gift.product.service;

import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductResponseDto createProduct(ProductRequestDto productRequestDto);

    Page<ProductResponseDto> findAllProducts(Pageable pageable);

    ProductResponseDto findProductById(Long id);

    ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto);

    void deleteProduct(Long id);
}
