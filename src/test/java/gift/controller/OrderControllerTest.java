package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.LoginMemberArgumentResolver;
import gift.domain.Member;
import gift.dto.OrderRequestDto;
import gift.enums.Role;
import gift.exception.UnauthorizedException;
import gift.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private LoginMemberArgumentResolver loginMemberArgumentResolver;

    private final Member mockMember = new Member(1L, "test@email.com", "encoded_pw", Role.ROLE_USER);

    @BeforeEach
    void setUp() {
        given(loginMemberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(mockMember);
    }

    @Test
    void 로그인한_사용자가_상품_주문_시_201반환() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        OrderRequestDto orderRequestDto = new OrderRequestDto(1L, 1, "test");
        String value = objectMapper.writeValueAsString(orderRequestDto);

        mockMvc.perform(
                        post("/api/orders")
                                .header("Authorization", "Bearer mock-token")
                                .contentType(APPLICATION_JSON)
                                .content(value))
                .andExpect(status().isCreated());
    }

    @Test
    void 로그인하지_않은_사용자가_상품_주문_시_401반환() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        OrderRequestDto orderRequestDto = new OrderRequestDto(1L, 1, "test");
        String value = objectMapper.writeValueAsString(orderRequestDto);

        doThrow(new UnauthorizedException("인증되지 않은 사용자", "order-api"))
                .when(loginMemberArgumentResolver)
                .resolveArgument(
                        org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.any()
                );

        mockMvc.perform(
                        post("/api/orders")
                                .header("Authorization", "Bearer mock-token")
                                .contentType(APPLICATION_JSON)
                                .content(value))
                .andExpect(status().isUnauthorized());
    }
}
