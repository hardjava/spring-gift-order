package gift.repository;

import gift.domain.Product;
import gift.domain.ProductName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    default Product findProductByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 ID의 상품을 찾을 수 없습니다."));
    }

    Optional<Product> findByName(ProductName name);

    @Query(
            value = """
                    SELECT p
                    FROM Product p
                    WHERE (:search IS NULL OR p.name.name LIKE CONCAT('%', :search, '%'))
                    """
    )
    Page<Product> findProductByNameLike(Pageable pageRequest, String search);
}
