package gift.service;

import gift.dto.ProductAdminRequestDto;
import gift.entity.Product;
import gift.repository.ProductRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductAdminService {

    private final ProductRepository productRepository;

    public ProductAdminService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional
    public Product save(ProductAdminRequestDto dto) {
        validateNameContent(dto.getName());
        Product product = dto.toEntity();
        return productRepository.save(product);
    }

    @Transactional
    public void update(Long id, ProductAdminRequestDto dto) {
        validateNameContent(dto.getName());

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "상품(id=" + id + ")을 찾을 수 없습니다."
                ));

        product.updateProduct(dto.getName(), dto.getPrice(), dto.getImageUrl());
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "상품(id=" + id + ")을 찾을 수 없습니다."
                ));
    }

    @Transactional
    public void deleteProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "상품(id=" + id + ")을 찾을 수 없습니다."
                ));
        productRepository.delete(product);

    }

    private void validateNameContent(String name) {
        if (name.contains("카카오")) {
            throw new IllegalArgumentException("‘카카오’가 포함된 문구는 담당 MD와 협의된 경우에만 사용할 수 있습니다.");
        }
    }

}
