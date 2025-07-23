package gift.option.exception;

import gift.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class DuplicateOptionException extends CustomException {

    public DuplicateOptionException() {
        super("같은 상품 내에 동일한 옵션명이 존재합니다.", HttpStatus.BAD_REQUEST);
    }
}
