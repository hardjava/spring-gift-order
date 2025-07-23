package gift.service;

import gift.dto.CreateOptionRequestDto;
import gift.dto.CreateProductRequestDto;
import gift.dto.OptionResponseDto;
import gift.dto.ProductPageDto;
import gift.dto.ProductResponseDto;
import gift.dto.UpdateProductRequestDto;
import gift.entity.Product;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductResponseDto createProduct(CreateProductRequestDto requestDto);

    ProductPageDto findAllProducts(Pageable pageable);

    ProductResponseDto findProductById(Long id);

    ProductResponseDto updateProductById(Long id, UpdateProductRequestDto requestDto);

    void deleteProductById(Long id);

    Product findProductByIdOrElseThrow(Long id);
}
