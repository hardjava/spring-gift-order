package gift.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;

@Configuration
public class RestClientConfig {
    private static final int CONNECTION_TIMEOUT = 5;
    private static final int READ_TIMEOUT = 5;

    @Bean
    public RestClient.Builder restClientBuilder() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(CONNECTION_TIMEOUT));
        requestFactory.setReadTimeout(Duration.ofSeconds(READ_TIMEOUT));

        return RestClient.builder()
                .requestFactory(requestFactory)
                .defaultStatusHandler(
                        statusCode -> statusCode.is4xxClientError() || statusCode.is5xxServerError(),
                        (request, response) -> {
                            if (response.getStatusCode().is5xxServerError()) {
                                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 서버 오류 발생");
                            }
                            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
                            }
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "요청이 잘못되었습니다.");
                        });
    }
}
