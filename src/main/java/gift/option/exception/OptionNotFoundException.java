package gift.option.exception;

import gift.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class OptionNotFoundException extends CustomException {
    public OptionNotFoundException(Long id) {
        super("해당 옵션을 찾을 수 없습니다. id=" + id, HttpStatus.NOT_FOUND);
    }
}
