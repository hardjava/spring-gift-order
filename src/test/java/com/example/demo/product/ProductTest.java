package com.example.demo.product;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.demo.dto.product.ProductRequestDto;
import com.example.demo.dto.product.ProductResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "jwt.secret=ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789012"
})
class ProductTest {

  @LocalServerPort
  private int port;

  private final RestClient client = RestClient.builder().build();

  private String baseUrl() {
    return "http://localhost:" + port + "/products";
  }

  private Long createProductAndGetId(String name, int price, String imageUrl) {
    ProductRequestDto dto = new ProductRequestDto(name, price, imageUrl);
    ResponseEntity<ProductResponseDto> response = client.post()
                                                        .uri(baseUrl())
                                                        .body(dto)
                                                        .retrieve()
                                                        .toEntity(ProductResponseDto.class);

    return response.getBody().id();
  }

  @Test
  void 유효한_상품정보로_생성하면_201이_반환된다() {
    ProductRequestDto dto = new ProductRequestDto("새상품", 1000, "http://image.com/1.jpg");
    ResponseEntity<ProductResponseDto> response = client.post()
                                                        .uri(baseUrl())
                                                        .body(dto)
                                                        .retrieve()
                                                        .toEntity(ProductResponseDto.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
  }

  @Test
  void 전체_상품을_조회하면_200이_반환된다() {
    ResponseEntity<ProductResponseDto[]> response = client.get()
                                                          .uri(baseUrl())
                                                          .retrieve()
                                                          .toEntity(ProductResponseDto[].class);

    assertAll(
        () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
        () -> assertThat(response.getBody()).isNotNull()
    );
  }

  @Test
  void 존재하는_상품아이디로_조회하면_200과_상품정보가_반환된다() {
    Long id = createProductAndGetId("테스트상품", 2000, "http://image.com/test.jpg");
    String url = baseUrl() + "/" + id;

    ResponseEntity<ProductResponseDto> response = client.get()
                                                        .uri(url)
                                                        .retrieve()
                                                        .toEntity(ProductResponseDto.class);

    assertAll(
        () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
        () -> assertThat(response.getBody()).isNotNull(),
        () -> assertThat(response.getBody().id()).isEqualTo(id)
    );
  }

  @Test
  void 존재하지_않는_상품ID로_조회하면_404가_반환된다() {
    String url = baseUrl() + "/999999";

    assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
        .isThrownBy(() ->
            client.get()
                  .uri(url)
                  .retrieve()
                  .toEntity(Void.class)
        );
  }

  @Test
  void 존재하는_상품아이디로_이름을_수정하면_200이_반환된다() {
    Long id = createProductAndGetId("수정전상품", 3000, "http://image.com/before.jpg");
    String url = baseUrl() + "/" + id;

    ProductRequestDto dto = new ProductRequestDto("수정된상품이름", 3000, "http://image.com/before.jpg");

    ResponseEntity<ProductResponseDto> response = client.patch()
                                                        .uri(url)
                                                        .body(dto)
                                                        .retrieve()
                                                        .toEntity(ProductResponseDto.class);

    assertAll(
        () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
        () -> assertThat(response.getBody()).isNotNull(),
        () -> assertThat(response.getBody().name()).isEqualTo("수정된상품이름")
    );
  }

  @Test
  void 존재하는_상품ID로_삭제하면_204가_반환된다() {
    Long id = createProductAndGetId("삭제할상품", 4000, "http://image.com/delete.jpg");
    String url = baseUrl() + "/" + id;

    ResponseEntity<Void> response = client.delete()
                                          .uri(url)
                                          .retrieve()
                                          .toEntity(Void.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }
}
