package gift.wish.repository;

import gift.wish.entity.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long> {
    @EntityGraph(attributePaths = "product")
    Page<Wish> findAllByMemberId(Long memberId, Pageable pageable);
    boolean existsByMemberIdAndProductId(Long memberId, Long productId);
}

