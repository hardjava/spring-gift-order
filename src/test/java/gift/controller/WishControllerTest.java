package gift.controller;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gift.dto.CreateMemberRequestDto;
import gift.dto.CreateWishRequestDto;
import gift.dto.DeleteMemberRequestDto;
import gift.dto.UpdateWishQuantityRequstDto;
import gift.dto.WishPageDto;
import gift.dto.WishResponseDto;
import gift.service.MemberService;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.Random.class)
public class WishControllerTest {

    String token;
    @LocalServerPort
    private int port;
    private RestClient client = RestClient.builder().build();

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("위시 조회 로그인하지 않을 시 실패 테스트")
    void 위시조회시_로그인_하지_않은_사용자는_401이_반환된다() {
        String url = "http://localhost:" + port + "/api/wishes";
        assertThatExceptionOfType(HttpClientErrorException.Unauthorized.class)
                .isThrownBy(() ->
                        client.get()
                                .uri(url)
                                .retrieve()
                                .toBodilessEntity()
                );
    }

    @Test
    @DisplayName("위시 등록 테스트")
    void 위시등록에_성공하면_201가_반환된다() {
        String url = "http://localhost:" + port + "/api/wishes";
        CreateWishRequestDto requestDto = new CreateWishRequestDto(7L, 3L);
        ResponseEntity<WishResponseDto> response = client.post()
                .uri(url)
                .header("Authorization", token)
                .body(requestDto)
                .retrieve()
                .toEntity(WishResponseDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("위시 조회 테스트")
    void 위시조회에_성공하면_200가_반환된다() {
        String url = "http://localhost:" + port + "/api/wishes";
        ResponseEntity<WishPageDto> response = client.get()
                .uri(url)
                .header("Authorization", token)
                .retrieve()
                .toEntity(WishPageDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("없는 위시 수정 시 실패 테스트")
    void 없는_위시의_수량변경하면_404가_반환된다() {
        String url = "http://localhost:" + port + "/api/wishes/999";
        UpdateWishQuantityRequstDto requestDto = new UpdateWishQuantityRequstDto(10L);
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(() ->
                        client.patch()
                                .uri(url)
                                .header("Authorization", token)
                                .body(requestDto)
                                .retrieve()
                                .toEntity(WishResponseDto.class)
                );
    }

    @Test
    @DisplayName("로그인 하지 않은 사용자 위시 수정 실패 테스트")
    void 위시수정시_로그인_하지_않은_사용자는_401이_반환된다() {
        String url = "http://localhost:" + port + "/api/wishes/1";
        UpdateWishQuantityRequstDto requestDto = new UpdateWishQuantityRequstDto(10L);

        assertThatExceptionOfType(HttpClientErrorException.Unauthorized.class)
                .isThrownBy(() ->
                        client.patch()
                                .uri(url)
                                .body(requestDto)
                                .retrieve()
                                .toBodilessEntity()
                );
    }

    @Test
    @DisplayName("위시 수량 변경 테스트")
    void 등록한_위시의_수량변경에_성공하면_200가_반환된다() {

        String url = "http://localhost:" + port + "/api/wishes/1";

        UpdateWishQuantityRequstDto updateRequestDto = new UpdateWishQuantityRequstDto(99L);
        ResponseEntity<WishResponseDto> response = client.patch()
                .uri(url)
                .header("Authorization", token)
                .body(updateRequestDto)
                .retrieve()
                .toEntity(WishResponseDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("로그인 하지 않을 시 위시 삭제 실패 테스트")
    void 위시삭제시_로그인_하지_않은_사용자는_401이_반환된다() {
        String url = "http://localhost:" + port + "/api/wishes/3";
        assertThatExceptionOfType(HttpClientErrorException.Unauthorized.class)
                .isThrownBy(() ->
                        client.delete()
                                .uri(url)
                                .retrieve()
                                .toBodilessEntity()
                );
    }

    @Test
    @DisplayName("없는 위시 삭제 시도 시 실패 테스트")
    void 없는_위시를_삭제하면_404가_반환된다() {
        String url = "http://localhost:" + port + "/api/wishes";
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(() ->
                        client.delete()
                                .uri(url + "/999")
                                .header("Authorization", token)
                                .retrieve()
                                .toBodilessEntity()
                );
    }

    @Test
    @DisplayName("위시 삭제 성공 테스트")
    void 등록한_위시의_삭제에_성공하면_204가_반환된다() {
        String url = "http://localhost:" + port + "/api/wishes";

        ResponseEntity<Void> response = client.delete()
                .uri(url + "/6")
                .header("Authorization", token)
                .retrieve()
                .toBodilessEntity();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @BeforeEach
    void CreateToken() {
        CreateMemberRequestDto requestDto = new CreateMemberRequestDto("testUser1@asdasd.asd",
                "asd");
        token = memberService.loginMember(requestDto).token();
    }
}
