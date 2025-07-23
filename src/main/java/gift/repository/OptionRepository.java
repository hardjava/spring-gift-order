package gift.repository;

import gift.entity.Option;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {

    Optional<Option> findByProduct_IdAndName(Long productId, String name);

    Optional<Option> findByProduct_IdAndId(Long productId, Long optionId);

    List<Option> findByProduct_Id(Long productId);
}
