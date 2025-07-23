package gift.repository;

import gift.entity.Member;
import gift.entity.Product;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    void deleteById(Long id);

}
