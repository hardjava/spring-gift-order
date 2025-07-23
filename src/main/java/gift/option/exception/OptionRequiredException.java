package gift.option.exception;

import gift.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class OptionRequiredException extends CustomException {

    public OptionRequiredException() {
        super("상품에는 하나 이상의 옵션이 필요합니다.", HttpStatus.BAD_REQUEST);
    }
}
