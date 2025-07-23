package com.example.demo.user;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import com.example.demo.dto.user.UserDataInfo;
import com.example.demo.dto.user.UserRequestDto;
import com.example.demo.jwt.Jwt;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "jwt.secret=ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789012"
})
@Transactional
class UserLoginTest {

  @LocalServerPort
  private int port;

  private final RestClient client = RestClient.builder().build();

  @Test
  void 로그인_후_me_요청시_회원정보가_반환된다() {
    UserRequestDto dto = new UserRequestDto("example@example.com", "abcd1234!", "admin");
    String loginUrl = "http://localhost:" + port + "/login";
    ResponseEntity<Jwt> loginResponse = client.post()
                                              .uri(loginUrl)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .body(dto)
                                              .retrieve()
                                              .toEntity(Jwt.class);

    String accessToken = loginResponse.getBody().getAccessToken();
    System.out.println("AccessToken: " + accessToken);

    String meUrl = "http://localhost:" + port + "/users/me";
    ResponseEntity<UserDataInfo> meResponse = client.get()
                                                    .uri(meUrl)
                                                    .header("Authorization", "Bearer " + accessToken)
                                                    .retrieve()
                                                    .toEntity(UserDataInfo.class);
    assertAll(
        () ->  assertThat(meResponse.getStatusCode()).isEqualTo(HttpStatus.OK),
        () -> assertThat(meResponse.getBody().email()).isEqualTo("example@example.com")
    );
  }

  @Test
  void 가입되지_않은_회원이_로그인하면_401이_반환된다() {
    UserRequestDto dto = new UserRequestDto("nonexist@example.com", "wrongpassword1!", "admin");
    String loginUrl = "http://localhost:" + port + "/login";

    assertThatExceptionOfType(HttpClientErrorException.Unauthorized.class)
        .isThrownBy(() ->
            client.post()
                  .uri(loginUrl)
                  .contentType(MediaType.APPLICATION_JSON)
                  .body(dto)
                  .retrieve()
                  .toEntity(Void.class)
        );
  }

  @Test
  void 가입된_회원이_로그인하면_200이_반환된다() {
    UserRequestDto dto = new UserRequestDto("example@example.com", "abcd1234!", "admin");
    String loginUrl = "http://localhost:" + port + "/login";

    ResponseEntity<Jwt> response = client.post()
                                         .uri(loginUrl)
                                         .contentType(MediaType.APPLICATION_JSON)
                                         .body(dto)
                                         .retrieve()
                                         .toEntity(Jwt.class);

    assertAll(
        () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
        () -> assertThat(response.getBody()).isNotNull(),
        () -> assertThat(response.getBody().getAccessToken()).isNotBlank(),
        () -> assertThat(response.getBody().getRefreshToken()).isNotBlank()
    );
  }
}
