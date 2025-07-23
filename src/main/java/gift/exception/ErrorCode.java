package gift.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    ProductNotfound(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다"),
    WishNotfound(HttpStatus.NOT_FOUND, "위시를 찾을 수 없습니다"),
    Unauthorized(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다"),
    AlreadyRegistered(HttpStatus.FORBIDDEN, "이미 가입한 이메일입니다"),
    AlreadyMadeWish(HttpStatus.FORBIDDEN, "이미 위시로 등록한 상품입니다"),
    NotLogin(HttpStatus.UNAUTHORIZED, "로그인 하지 않았습니다"),
    NotRegisterd(HttpStatus.NOT_FOUND, "해당 이메일은 가입하지 않았습니다"),
    AlreadyExistOptionName(HttpStatus.BAD_REQUEST, "중복된 옵션은 추가할 수 없습니다"),
    NamingForbidden(HttpStatus.FORBIDDEN, "'카카오'가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다"),
    OptionNotFound(HttpStatus.NOT_FOUND, "없는 옵션입니다"),
    OptionNotEnough(HttpStatus.FORBIDDEN, "선택한 옵션의 수량이 부족합니다");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
