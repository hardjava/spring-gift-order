package gift.repository;

import gift.domain.Member;
import gift.domain.OauthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public interface OauthTokenRepository extends JpaRepository<OauthToken, Long> {
    default OauthToken findByMemberOrElseThrow(Member member) {
        return findByMember(member).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 토큰을 찾을 수 없습니다."));
    }

    Optional<OauthToken> findByMember(Member member);

    void deleteByMember(Member member);
}
