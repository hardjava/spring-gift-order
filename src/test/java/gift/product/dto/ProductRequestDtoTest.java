package gift.product.dto;

import gift.option.dto.OptionCreateRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.sql.init.mode=never"
)
class ProductRequestDtoTest {
    @LocalServerPort
    private int port;

    private RestClient client = RestClient.builder().build();
    private String url;

    @BeforeEach
    void setUp() {
        url = "http://localhost:" + port + "/api/products";
    }

    // 생성 테스트
    @Test
    @DisplayName("Product 생성 시, 검증된 input을 넣으면 201(Created)이 반환된다. ")
    void createProduct_withValidInput_returns201() {
        ProductRequestDto productRequestDto = createProductRequestDto(
                "하리보 젤리(콜라맛)",
                2000,
                "http://img.url/test.png",
                List.of(new OptionCreateRequestDto("낱개", 1))
        );

        var response = client.post()
                .uri(url)
                .body(productRequestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ProductResponseDto body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.name()).isEqualTo("하리보 젤리(콜라맛)");
        assertThat(body.price()).isEqualTo(2000);
        assertThat(body.imageUrl()).isEqualTo("http://img.url/test.png");
    }

    @Test
    @DisplayName("Product 생성 시, 상품명이 15자를 초과한 경우 400(Bad Request)를 반환한다. ")
    void createProduct_withNameTooLong_returns400() {
        String name = "하리보맛있네또사먹을게요많이파세요"; // 총 17자
        ProductRequestDto productRequestDto = createProductRequestDto(
                name,
                50,
                "http://img.url/test.png",
                List.of(new OptionCreateRequestDto("낱개", 1))
        );

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(() ->
                        client.post()
                                .uri(url)
                                .body(productRequestDto)
                                .retrieve()
                                .toEntity(Void.class)
                )
                .satisfies(ex -> {
                    String msg = ex.getResponseBodyAsString();
                    assertThat(msg).contains("상품명은 최대 15자까지 입력할 수 있습니다.");
                });
    }

    @Test
    @DisplayName("Product 생성 시, 허용되지 않은 특수문자를 포함한 경우 400(Bad Request)를 반환한다. ")
    void createProduct_withBadCharacters_returns400() {
        ProductRequestDto productRequestDto = createProductRequestDto(
                "하리보!@",
                20,
                "http://img.url/test.png",
                List.of(new OptionCreateRequestDto("낱개", 1))
        );

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(() ->
                        client.post()
                                .uri(url)
                                .body(productRequestDto)
                                .retrieve()
                                .toEntity(Void.class)
                )
                .satisfies(ex -> {
                    String msg = ex.getResponseBodyAsString();
                    assertThat(msg).contains("상품 이름에 허용되지 않는 문자가 포함되어 있습니다.");
                });
    }

    @Test
    @DisplayName("Product 생성 시, 음수 가격을 입력한 경우 400(Bad Request)를 반환한다. ")
    void createProduct_withNegativePrice_returns400() {
        ProductRequestDto productRequestDto = createProductRequestDto(
                "하리보 젤리",
                -2000,
                "http://img.url/test.png"
                , List.of(new OptionCreateRequestDto("낱", 1))
        );

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(() ->
                        client.post()
                                .uri(url)
                                .body(productRequestDto)
                                .retrieve()
                                .toEntity(Void.class)
                )
                .satisfies(ex -> {
                    String msg = ex.getResponseBodyAsString();
                    assertThat(msg).contains("가격은 0 이상이어야 합니다.");
                });
    }

    @Test
    @DisplayName("Product 생성 시, '카카오'가 포함된 문구를 입력한 경우 400(Bad Request)를 반환한다. ")
    void createProduct_withKakaoInName_returns400() {
        ProductRequestDto productRequestDto = createProductRequestDto(
                "카카오 젤리",
                3000,
                "http://img.url/test.png"
                , List.of(new OptionCreateRequestDto("낱개", 1))
        );

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(() ->
                        client.post()
                                .uri(url)
                                .body(productRequestDto)
                                .retrieve()
                                .toEntity(Void.class)
                )
                .satisfies(ex -> {
                    String msg = ex.getResponseBodyAsString();
                    assertThat(msg).contains("'카카오'가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.");
                });
    }

    // 조회 테스트
    @Test
    @DisplayName("Product 조회 시, 존재하는 ID로 조회하면 200(OK)을 반환한다. ")
    void findProduct_withExistingId_returns200() {
        var productRequestDto = createProductRequestDto(
                "하리보 젤리(지렁이)",
                3000,
                "http://img.url/test.png"
                , List.of(new OptionCreateRequestDto("낱개", 1))
        );

        var created = client.post()
                .uri(url)
                .body(productRequestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class)
                .getBody();
        Long id = created.id();

        var response = client.get()
                .uri(url + "/" + id)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().name()).isEqualTo("하리보 젤리(지렁이)");
    }

    @Test
    @DisplayName("Product 조회 시, 존재하지 않는 ID로 조회하면 404(Not Found)를 반환한다. ")
    void findProduct_withNonExistingId_returns404() {
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(() ->
                        client.get()
                                .uri(url + "/9999")
                                .retrieve()
                                .toEntity(Void.class)
                );
    }

    // 수정 테스트
    @Test
    @DisplayName("Product 수정 시, 수정할 내용에 대한 input을 넣으면 200(OK)을 반환한다. ")
    void updateProduct_withValidInput_returns200() {
        ProductRequestDto productRequestDto = createProductRequestDto(
                "하리보 젤리(오리지널)",
                1500,
                "http://img.url/original.png"
                , List.of(new OptionCreateRequestDto("낱개", 1))
        );

        var created = client.post()
                .uri(url)
                .body(productRequestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class)
                .getBody();
        assertThat(created).isNotNull();

        ProductRequestDto updateDto = createProductRequestDto(
                "하리보 젤리(리뉴얼)",
                2000,
                "http://img.url/new.png"
                , List.of(new OptionCreateRequestDto("낱개", 1))
        );

        var response = client.put()
                .uri(url + "/" + created.id())
                .body(updateDto)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ProductResponseDto updated = response.getBody();
        assertThat(updated).isNotNull();
        assertThat(updated.name()).isEqualTo("하리보 젤리(리뉴얼)");
        assertThat(updated.price()).isEqualTo(2000);
        assertThat(updated.imageUrl()).isEqualTo("http://img.url/new.png");
    }

    // 삭제 테스트
    @Test
    @DisplayName("Product 삭제 시, 존재하는 ID면 204(No Content)를 반환한다. ")
    void deleteProduct_withExistingId_returns204() {
        ProductRequestDto productRequestDto = createProductRequestDto(
                "하리보 젤리",
                1500,
                "http://img.url/test.png"
                , List.of(new OptionCreateRequestDto("낱개", 1))
        );

        var created = client.post()
                .uri(url)
                .body(productRequestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class)
                .getBody();

        var deleteResponse = client.delete()
                .uri(url + "/" + created.id())
                .retrieve()
                .toBodilessEntity();

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Product 삭제 시, 존재하지 않는 ID면 404(Not Found)를 반환한다")
    void deleteProduct_withNonExistingId_returns404() {
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(() ->
                        client.delete()
                                .uri(url + "/9999")
                                .retrieve()
                                .toBodilessEntity()
                );
    }

    private ProductRequestDto createProductRequestDto(String name, Integer price, String imageUrl, List<OptionCreateRequestDto> options) {
        return new ProductRequestDto(name, price, imageUrl, options);
    }
}
