package gift.exception;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return httpResponse.getStatusCode().is5xxServerError() ||
               httpResponse.getStatusCode().is4xxClientError();
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().is5xxServerError()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 서버 오류 발생");
        } else if (response.getStatusCode().is4xxClientError()) {
            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "요청이 잘못되었습니다.");
        }
    }
}
