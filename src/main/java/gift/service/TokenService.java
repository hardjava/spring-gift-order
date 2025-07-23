package gift.service;

import gift.entity.Member;
import java.util.Optional;

public interface TokenService {

    Member isValidateToken(String token);

    String createAccessToken(Member member);
}
