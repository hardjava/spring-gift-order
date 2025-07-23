package gift.service;

import gift.domain.PaginationInfo;
import gift.dto.*;
import gift.domain.Product;
import gift.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductWithPageResponseDto findAllProducts(PaginationInfo paginationInfo) {
        Page<Product> findProducts = productRepository.findProductByNameLike(
                paginationInfo.getPageable(),
                paginationInfo.getSearch()
        );

        return ProductWithPageResponseDto.from(findProducts);
    }

    public ProductResponseDto findProductById(Long id) {
        Product findProduct = productRepository.findProductByIdOrElseThrow(id);

        return ProductResponseDto.from(findProduct);
    }

    @Transactional
    public ProductResponseDto createProduct(CreateProductRequestDto requestDto) {
        Product newProduct = Product.from(requestDto);
        Product savedProduct = productRepository.save(newProduct);

        return ProductResponseDto.from(savedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product findProduct = productRepository.findProductByIdOrElseThrow(id);
        productRepository.deleteById(id);
    }

    @Transactional
    public void updateProduct(UpdateProductRequestDto requestDto) {
        Product findProduct = productRepository.findProductByIdOrElseThrow(requestDto.id());
        findProduct.update(requestDto.name(), requestDto.price(), requestDto.imageUrl());
    }
}
