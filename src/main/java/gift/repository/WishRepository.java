package gift.repository;

import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.misc.Pair;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long> {

    Page<Wish> findAllByMember_Id(Long memberId, Pageable pageable);

    Optional<Wish> findByProduct_IdAndMember_Id(Long productId, Long memberId);

    void deleteByProduct_IdAndMember_Id(Long productId, Long memberId);
}