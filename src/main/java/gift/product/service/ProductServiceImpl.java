package gift.product.service;

import gift.option.entity.Option;
import gift.option.exception.OptionRequiredException;
import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.entity.Product;
import gift.product.exception.ProductNotFoundException;
import gift.product.repository.ProductRepository;
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
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        if (productRequestDto.options() == null || productRequestDto.options().isEmpty()) {
            throw new OptionRequiredException();
        }

        Product product = new Product(
                null,
                productRequestDto.name(),
                productRequestDto.price(),
                productRequestDto.imageUrl()
        );
        Product created = productRepository.save(product);

        productRequestDto.options().forEach(optDto ->
                created.addOption(new Option(created, optDto.name(), optDto.quantity()))
        );

        return ProductResponseDto.from(created);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> findAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductResponseDto::from);
    }


    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto findProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return ProductResponseDto.from(product);
    }

    @Override
    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.update(productRequestDto.name(), productRequestDto.price(), productRequestDto.imageUrl());

        return ProductResponseDto.from(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        productRepository.delete(product);
    }
}
