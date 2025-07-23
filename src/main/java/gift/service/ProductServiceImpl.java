package gift.service;

import gift.dto.CreateOptionRequestDto;
import gift.dto.CreateProductRequestDto;
import gift.dto.OptionResponseDto;
import gift.dto.ProductPageDto;
import gift.dto.ProductResponseDto;
import gift.dto.UpdateProductRequestDto;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public ProductResponseDto createProduct(CreateProductRequestDto requestDto) {
        List<Option> optionList = new ArrayList<>();
        Product newProduct = new Product(requestDto.name(), requestDto.price(),
                requestDto.imageUrl(), optionList);
        for (CreateOptionRequestDto requestOptionDto : requestDto.options()){
            Option option = new Option(requestOptionDto.name(), requestOptionDto.quantity(), newProduct);
            newProduct.addOption(option);
        }
        Product savedProduct = productRepository.save(newProduct);
        return new ProductResponseDto(savedProduct.getId(), savedProduct.getName(),
                savedProduct.getPrice(), savedProduct.getImageUrl());
    }

    @Override
    public ProductPageDto findAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        Page<ProductResponseDto> responseDtos = toResponseDtoPage(products);
        return new ProductPageDto(responseDtos);
    }

    @Override
    public ProductResponseDto findProductById(Long id) {
        Product product = findProductByIdOrElseThrow(id);

        return new ProductResponseDto(product.getId(), product.getName(),
                product.getPrice(), product.getImageUrl());
    }

    @Override
    @Transactional
    public ProductResponseDto updateProductById(Long id, UpdateProductRequestDto requestDto) {
        Product product = findProductByIdOrElseThrow(id);
        product.changeName(requestDto.name());
        product.changePrice(requestDto.price());
        product.changeImageUrl(requestDto.imageUrl());
        Product updated = findProductByIdOrElseThrow(id);
        return new ProductResponseDto(updated.getId(), updated.getName(), updated.getPrice(),
                updated.getImageUrl());
    }

    @Override
    @Transactional
    public void deleteProductById(Long id) {
        findProductByIdOrElseThrow(id);
        productRepository.deleteById(id);
    }

    @Override
    public Product findProductByIdOrElseThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ProductNotfound));
    }

    private Page<ProductResponseDto> toResponseDtoPage(Page<Product> page) {
        return page.map(product -> new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl()));
    }
}
