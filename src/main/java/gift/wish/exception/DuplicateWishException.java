package gift.wish.exception;

import gift.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class DuplicateWishException extends CustomException {
    private final Long memberId;
    private final Long productId;

    public DuplicateWishException(Long memberId, Long productId) {
        super(String.format("회원 %d의 위시리스트에 상품 %d가 이미 존재합니다.", memberId, productId), HttpStatus.CONFLICT);
        this.memberId = memberId;
        this.productId = productId;
    }
}