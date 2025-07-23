package gift.security.exception;

import gift.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class AccessDeniedException extends CustomException {
    public AccessDeniedException(Long memberId) {
        super("이 위시를 삭제할 권한이 없습니다. memberId: " + memberId, HttpStatus.FORBIDDEN);
    }
}
