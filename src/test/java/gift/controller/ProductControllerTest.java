package gift.controller;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gift.dto.CreateMemberRequestDto;
import gift.dto.CreateOptionRequestDto;
import gift.dto.CreateProductRequestDto;
import gift.dto.DeleteMemberRequestDto;
import gift.dto.OptionResponseDto;
import gift.dto.ProductPageDto;
import gift.dto.ProductResponseDto;
import gift.dto.UpdateOptionQuantityRequestDto;
import gift.dto.UpdateProductRequestDto;
import gift.dto.WishPageDto;
import gift.entity.Product;
import gift.service.MemberService;
import gift.service.ProductService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.Random.class)
public class ProductControllerTest {

    String token;
    @LocalServerPort
    private int port;
    private RestClient client = RestClient.builder().build();
    @Autowired
    private ProductService productService;
    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("상품 전체 조회 테스트")
    void 전체_조회하면_200이_반환된다() {
        String url = "http://localhost:" + port + "/api/products";
        ResponseEntity<ProductPageDto> response = client.get()
                .uri(url)
                .retrieve()
                .toEntity(ProductPageDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("상품 개별 조회 테스트")
    void 존재하는_아이디로_개별조회하면_200이_반환된다() {
        String url = "http://localhost:" + port + "/api/products/3";
        ResponseEntity<ProductResponseDto> response = client.get()
                .uri(url)
                .retrieve()
                .toEntity(ProductResponseDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("없는 상품 개별 조회 실패 테스트")
    void 존재하지_않는_아이디로_개별조회하면_404가_반환된다() {
        String url = "http://localhost:" + port + "/api/products/999";
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(() ->
                        client.get()
                                .uri(url)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }

    @Test
    @DisplayName("상품 등록 성공 테스트")
    void 상품등록에_성공하면_201가_반환된다() {
        String url = "http://localhost:" + port + "/api/products";
        CreateOptionRequestDto optionRequestDto = new CreateOptionRequestDto("asd", 1L);
        List<CreateOptionRequestDto> optionRequestDtos = new ArrayList<>();
        optionRequestDtos.add(optionRequestDto);
        CreateProductRequestDto requestDto = new CreateProductRequestDto("asd", 123L, "aasdfgh",optionRequestDtos);
        ResponseEntity<Product> response = client.post()
                .uri(url)
                .header("Authorization", token)
                .body(requestDto)
                .retrieve()
                .toEntity(Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("입력 값 검증으로 인한 상품 등록 실패 테스트")
    void 상품등록에_실패하면_400가_반환된다() {
        String url = "http://localhost:" + port + "/api/products";
        CreateOptionRequestDto optionRequestDto = new CreateOptionRequestDto("asd", 1L);
        List<CreateOptionRequestDto> optionRequestDtos = new ArrayList<>();
        optionRequestDtos.add(optionRequestDto);
        CreateProductRequestDto requestDto = new CreateProductRequestDto(
                "asdasdasdasdasdasdasdasdasdasd", 123L, "aasdfgh",optionRequestDtos);
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(() ->
                        client.post()
                                .uri(url)
                                .header("Authorization", token)
                                .body(requestDto)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }

    @Test
    @DisplayName("입력 값 검증으로 인한 상품 수정 실패 테스트")
    void 상품수정에_실패하면_400가_반환된다() {
        String url = "http://localhost:" + port + "/api/products/1";
        UpdateProductRequestDto requestDto = new UpdateProductRequestDto(
                "asdasdasdasdasdasdasdasdasdasd", 123L, "aasdfgh");
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(() ->
                        client.put()
                                .uri(url)
                                .header("Authorization", token)
                                .body(requestDto)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }

    @Test
    @DisplayName("없는 상품 수정 실패 테스트")
    void 없는_상품을_수정하면_404가_반환된다() {
        String url = "http://localhost:" + port + "/api/products/999";
        UpdateProductRequestDto requestDto = new UpdateProductRequestDto("asd", 123L, "aasdfgh");
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(() ->
                        client.put()
                                .uri(url)
                                .header("Authorization", token)
                                .body(requestDto)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }

    @Test
    @DisplayName("상품 수정 성공 테스트")
    void 상품을_정상적으로_수정하면_200가_반환된다() {
        String url = "http://localhost:" + port + "/api/products/3";
        UpdateProductRequestDto requestDto = new UpdateProductRequestDto("asd", 123L, "aasdfgh");
        ResponseEntity<ProductResponseDto> response = client.put()
                .uri(url)
                .header("Authorization", token)
                .body(requestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("상품 삭제 성공 테스트")
    void 상품을_정상적으로_삭제하면_204가_반환된다() {
        String url = "http://localhost:" + port + "/api/products/2";
        ResponseEntity<Void> response = client.delete()
                .uri(url)
                .header("Authorization", token)
                .retrieve()
                .toBodilessEntity();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("없는 상품 삭제 실패 테스트")
    void 상품을_삭제_실패하면_404가_반환된다() {
        String url = "http://localhost:" + port + "/api/products/999";

        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(() ->
                        client.delete()
                                .uri(url)
                                .header("Authorization", token)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }

    @Test
    @DisplayName("없는 옵션 삭제 실패 테스트")
    void 옵션을_삭제_실패하면_404가_반환된다() {
        String url = "http://localhost:" + port + "/api/products/1/options/999";

        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(() ->
                        client.delete()
                                .uri(url)
                                .header("Authorization", token)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }

    @Test
    @DisplayName("입력 값 검증 (이름 제한 초과) 으로 인한 옵션 등록 실패 테스트")
    void 옵션을_등록_실패하면_404가_반환된다() {
        String url = "http://localhost:" + port + "/api/products/1/options";
        CreateOptionRequestDto requestDto = new CreateOptionRequestDto("a".repeat(51), 1L);
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(() ->
                        client.post()
                                .uri(url)
                                .header("Authorization", token)
                                .body(requestDto)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }

    @Test
    @DisplayName("입력 값 검증 (수량 제한 초과) 으로 인한 옵션 등록 실패 테스트")
    void 수량이_초과해_옵션을_등록_실패하면_404가_반환된다() {
        String url = "http://localhost:" + port + "/api/products/1/options";
        CreateOptionRequestDto requestDto = new CreateOptionRequestDto("a", 1000000000L);
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(() ->
                        client.post()
                                .uri(url)
                                .header("Authorization", token)
                                .body(requestDto)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }

    @Test
    @DisplayName("옵션 등록 성공 테스트")
    void 옵션_등록에_성공하면_201가_반환된다() {
        String url = "http://localhost:" + port + "/api/products/1/options";
        CreateOptionRequestDto requestDto = new CreateOptionRequestDto("asd", 1L);
        ResponseEntity<Product> response = client.post()
                .uri(url)
                .header("Authorization", token)
                .body(requestDto)
                .retrieve()
                .toEntity(Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("옵션 삭제 성공 테스트")
    void 옵션을_정상적으로_삭제하면_204가_반환된다() {
        String url = "http://localhost:" + port + "/api/products/5/options/10";
        ResponseEntity<Void> response = client.delete()
                .uri(url)
                .header("Authorization", token)
                .retrieve()
                .toBodilessEntity();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("옵션 수정 성공 테스트")
    void 옵션을_정상적으로_수정하면_200가_반환된다() {
        String url = "http://localhost:" + port + "/api/products/1/options/3";
        UpdateOptionQuantityRequestDto requestDto = new UpdateOptionQuantityRequestDto(999L);
        ResponseEntity<Void> response = client.patch()
                .uri(url)
                .header("Authorization", token)
                .body(requestDto)
                .retrieve()
                .toBodilessEntity();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("없는 옵션 수정 실패 테스트")
    void 없는_옵션을_수정하면_404가_반환된다() {
        String url = "http://localhost:" + port + "/api/products/1/options/999";
        UpdateOptionQuantityRequestDto requestDto = new UpdateOptionQuantityRequestDto(999L);
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(() ->
                        client.patch()
                                .uri(url)
                                .header("Authorization", token)
                                .body(requestDto)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }

    @Test
    @DisplayName("옵션 조회 테스트")
    void 옵션_조회하면_200이_반환된다() {
        String url = "http://localhost:" + port + "/api/products/3/options";
        ResponseEntity<List<OptionResponseDto>> response = client.get()
                .uri(url)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<OptionResponseDto>>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @AfterEach
    void deleteTestProduct() {
        try {
            productService.deleteProductById(4L);
        } catch (Exception e) {

        }
    }

    @BeforeEach
    void CreateToken() {
        CreateMemberRequestDto requestDto = new CreateMemberRequestDto("testUser1@asdasd.asd",
                "asd");
        token = memberService.loginMember(requestDto).token();
    }
}
