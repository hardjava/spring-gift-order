package gift.exception;

import gift.config.RestClientConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest
public class RestClientResponseErrorTest {
    @Autowired
    private RestClientConfig config;
    private RestClient restClient;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = config.restClientBuilder();
        mockServer = MockRestServiceServer.bindTo(builder).build();
        restClient = builder.build();
    }

    @Test
    void 에러테스트_5xx() {
        // given
        String url = "https://test.com/error";

        mockServer.expect(requestTo(url))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        // when & then
        assertThatThrownBy(() ->
                restClient.get()
                        .uri(url)
                        .retrieve()
                        .body(String.class)
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
                        restClient.get()
                                .uri(url)
                                .retrieve()
                                .body(String.class)
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
                restClient.get()
                        .uri(url)
                        .retrieve()
                        .body(String.class)
        )
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("사용자를 찾을 수 없습니다.");
    }
}
