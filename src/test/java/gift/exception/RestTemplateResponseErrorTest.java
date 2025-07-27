package gift.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

public class RestTemplateResponseErrorTest {
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void 에러테스트_5xx() {
        // given
        String url = "https://test.com/error";

        mockServer.expect(requestTo(url))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        // when & then
        assertThatThrownBy(() ->
                restTemplate.exchange(url, HttpMethod.GET, null, String.class)
        )
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("카카오 서버 오류 발생");
    }

    @Test
    void 잘못된_요청_에러() {
        // given
        String url = "https://test.com/error";

        mockServer.expect(requestTo(url))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        // when & then
        assertThatThrownBy(() ->
                restTemplate.exchange(url, HttpMethod.GET, null, String.class)
        )
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("요청이 잘못되었습니다.");
    }

    @Test
    void not_found_에러() {
        // given
        String url = "https://test.com/error";

        mockServer.expect(requestTo(url))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        // when & then
        assertThatThrownBy(() ->
                restTemplate.exchange(url, HttpMethod.GET, null, String.class)
        )
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("사용자를 찾을 수 없습니다.");
    }
}
