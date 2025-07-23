package gift.wish.exception;

import gift.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class WishNotFoundException extends CustomException {
    public WishNotFoundException(Long id) {
        super("존재하지 않는 위시입니다. id=" + id, HttpStatus.NOT_FOUND);
    }
}