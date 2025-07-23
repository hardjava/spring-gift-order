package gift.controller;

import gift.auth.LoginMemberArgumentResolver;
import gift.domain.Member;
import gift.enums.Role;
import gift.exception.UnauthorizedException;
import gift.service.WishListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.BDDMockito.given;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebMvcTest(WishListController.class)
public class WishListControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WishListService wishListService;

    @MockitoBean
    private LoginMemberArgumentResolver loginMemberArgumentResolver;

    private final Member mockMember = new Member(1L, "test@email.com", "encoded_pw", Role.ROLE_USER);

    @BeforeEach
    void setUp() {
        // 인증 리졸버가 member 반환하도록 설정
        given(loginMemberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(mockMember);
    }

    @Test
    void 로그인한_시용자가_위시리스트_조회_시_200반환() throws Exception {
        mockMvc.perform(get("/api/wishes")
                        .header("Authorization", "Bearer mock-token")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void 로그인하지_않은_사용자가_위시리스트_조회_시_401반환() throws Exception {
        doThrow(new UnauthorizedException("인증되지 않은 사용자입니다", "wishlist-api"))
                .when(loginMemberArgumentResolver)
                .resolveArgument(
                        org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.any()
                );

        mockMvc.perform(get("/api/wishes")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 로그인한_사용자가_위시리스트에_상품_등록_시_201반환() throws Exception {
        // given
        String requestBody = """
                {
                    "productId": 101
                }
                """;
        // when & then
        mockMvc.perform(
                        post("/api/wishes")
                                .header("Authorization", "Bearer mock-token")
                                .contentType(APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    void 로그인한_사용자가_상품_삭제_시_204반환() throws Exception {
        Long productId = 101L;

        mockMvc.perform(
                        delete("/api/wishes/{productId}", productId)
                                .header("Authorization", "Bearer mock-token"))
                .andExpect(status().isNoContent());
    }
}
