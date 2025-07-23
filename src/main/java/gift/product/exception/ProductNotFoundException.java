package gift.product.exception;

import gift.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends CustomException {
    public ProductNotFoundException(Long id) {
        super("해당 상품을 찾을 수 없습니다. id=" + id, HttpStatus.NOT_FOUND);
    }
}