package gift.repository;

import gift.domain.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findByProductId(Long productId);

    default List<Option> findByProductIdOrElseThrow(Long productId) {
        List<Option> options = findByProductId(productId);
        if (options.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 ID의 상품을 찾을 수 없습니다.");
        }

        return options;
    }

    default Option findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 옵션을 찾을 수 없습니다."));
    }
}
