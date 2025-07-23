package gift.member.exception;

import gift.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends CustomException {
    public InvalidPasswordException() {
        super("비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);
    }
}
