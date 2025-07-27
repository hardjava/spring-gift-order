package gift.repository;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import gift.domain.WishSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WishListRepository extends JpaRepository<Wish, Long> {

    boolean existsWishByMemberIdAndProductId(Long memberId, Long productId);

    void deleteWishByMemberIdAndProductId(Long memberId, Long productId);

    @Query(
            value = """ 
                    SELECT new gift.domain.WishSummary(p.name.name, COUNT(w))
                    FROM Wish w
                    JOIN w.product p
                    WHERE w.member.id = :memberId
                    AND (:search IS NULL OR p.name.name LIKE CONCAT('%', :search, '%'))
                    GROUP BY p.name.name
                    """,
            countQuery = """
                    SELECT COUNT(DISTINCT p.name.name)
                    FROM Wish w
                    JOIN w.product p
                    WHERE w.member.id = :memberId
                    AND (:search IS NULL OR p.name.name LIKE CONCAT('%', :search, '%'))
                    """
    )
    Page<WishSummary> findWishSummaryByMemberId(@Param("memberId") Long memberId, @Param("search") String search, Pageable pageable);

    void deleteWishesByMemberAndProduct(Member member, Product product);

}
