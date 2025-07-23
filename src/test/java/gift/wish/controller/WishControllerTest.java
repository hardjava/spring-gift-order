package gift.wish.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.member.entity.Member;
import gift.member.entity.Role;
import gift.member.repository.MemberRepository;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import gift.security.config.JwtProvider;
import gift.wish.dto.WishRequestDto;
import gift.wish.entity.Wish;
import gift.wish.repository.WishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WishControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    WishRepository wishRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    JwtProvider jwtProvider;

    private Member member;
    private Product product;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        wishRepository.deleteAll();
        productRepository.deleteAll();
        memberRepository.deleteAll();

        member = memberRepository.save(
                new Member("솨야", "wish@test.com", "pw", Role.USER)
        );
        product = productRepository.save(
                new Product(null, "하리보 젤리", 1500, "http://img.url/test.png")
        );

        jwtToken = jwtProvider.generateToken(member);
    }

    @Test
    @DisplayName("위시를 추가하면, 해당 상품 정보가 담긴 응답을 반환한다.")
    void shouldAddWish() throws Exception {
        // given
        var dto = new WishRequestDto(product.getId());

        // when & then
        mockMvc.perform(post("/api/wishes")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(product.getId()))
                .andExpect(jsonPath("$.productName").value("하리보 젤리"));
    }

    @Test
    @DisplayName("회원의 위시 목록을 조회하면, 해당 회원의 위시 목록을 반환한다.")
    void shouldGetWishes() throws Exception {
        var dto = new WishRequestDto(product.getId());
        mockMvc.perform(post("/api/wishes")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/wishes")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].productName").value("하리보 젤리"));
    }

    @Test
    @DisplayName("위시 ID로 삭제 요청하면, 204(No Content)를 반환한다.")
    void shouldDeleteWish() throws Exception {
        // given
        Wish savedWish = wishRepository.save(new Wish(member, product));

        // when & then
        mockMvc.perform(delete("/api/wishes/" + savedWish.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNoContent());
    }
}
