package gift.exception;

import org.springframework.http.HttpStatus;

public record CustomErrorResponse(HttpStatus httpStatus, String message) {

}
