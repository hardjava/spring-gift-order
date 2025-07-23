package gift.service;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.entity.Option;
import gift.entity.Product;
import gift.repository.ProductRepository;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    private Product toEntity(ProductRequestDto dto) {
        return new Product(dto.getName(), dto.getPrice(), dto.getImageUrl());
    }

    private ProductResponseDto toDto(Product product) {

        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl()
        );

    }



    public Page<ProductResponseDto> findAllProduct(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::toDto);
    }


    @Transactional
    public ProductResponseDto addProduct(ProductRequestDto productRequestDto) {
        validateNameContent(productRequestDto.getName());

        Product product = new Product(
                productRequestDto.getName(),
                productRequestDto.getPrice(),
                productRequestDto.getImageUrl()
        );
        productRequestDto.getOptions().forEach(optionDto -> {
            product.addOption(new Option(null, optionDto.name(), optionDto.quantity()));
        });
        Product savedProduct = productRepository.save(product);
        return toDto(savedProduct);
    }

    public ProductResponseDto findProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("product does not exist."));
        return toDto(product);
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto) {
        validateNameContent(productRequestDto.getName());

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("product does not exist."));

        product.updateProduct(
                productRequestDto.getName(),
                productRequestDto.getPrice(),
                productRequestDto.getImageUrl()
        );

        return toDto(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product does not exist."));
        productRepository.delete(product);
    }

    public List<Product> findAllById(List<Long> ids) {
        return productRepository.findAllById(ids);

    }

    private void validateNameContent(String name) {
        if (name.contains("카카오")) {
            throw new IllegalArgumentException("상품 이름은 '카카오'를 포함할 수 없습니다.");
        }
    }

    public Product findProductEntity(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found. id=" + id));
    }


}
