package gift.member.exception;

import gift.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends CustomException {
    public MemberNotFoundException(Long id) {
        super("해당 회원을 찾을 수 없습니다. id=" + id, HttpStatus.NOT_FOUND);
    }
}
