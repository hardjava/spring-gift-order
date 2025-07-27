package gift.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.component.JwtUtil;
import gift.config.KakaoOauthConfig;
import gift.domain.KakaoLoginResponse;
import gift.domain.KakaoUserInfo;
import gift.domain.Member;
import gift.dto.TokenResponseDto;
import gift.enums.OauthProvider;
import gift.repository.MemberRepository;
import gift.repository.OauthTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KakaoServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private OauthTokenRepository oauthTokenRepository;

    @Mock
    private KakaoOauthConfig kakaoOauthConfig;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    KakaoService kakaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(restTemplateBuilder.errorHandler(any())).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        kakaoService = new KakaoService(memberRepository, oauthTokenRepository, kakaoOauthConfig, restTemplateBuilder, jwtUtil, objectMapper);
    }

    @Test
    void 카카오_로그인_신규회원이면_회원가입후_토큰발급() {
        String code = "auth-code";
        KakaoLoginResponse loginResponse = new KakaoLoginResponse("access-token", "test", "test", "test", 123, 123);
        KakaoUserInfo userInfo = new KakaoUserInfo(456L);
        Member newMember = new Member(456L, OauthProvider.PROVIDER_KAKAO);

        when(restTemplate.exchange(any(RequestEntity.class), eq(KakaoLoginResponse.class)))
                .thenReturn(ResponseEntity.ok(loginResponse));
        when(restTemplate.exchange(any(RequestEntity.class), eq(KakaoUserInfo.class)))
                .thenReturn(ResponseEntity.ok(userInfo));
        when(memberRepository.findMemberByOauthIdAndOauthProvider(eq(456L), eq(OauthProvider.PROVIDER_KAKAO)))
                .thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenReturn(newMember);
        when(jwtUtil.createToken(newMember)).thenReturn("mock.new.token");

        TokenResponseDto token = kakaoService.kakaoLogin(code);
        assertThat(token.token()).isEqualTo("mock.new.token");
    }

    @Test
    void 카카오_로그인_기존회원이면_토큰발급() {
        String code = "auth-code";
        KakaoLoginResponse loginResponse = new KakaoLoginResponse("access-token", "test", "test", "test", 123, 123);
        KakaoUserInfo userInfo = new KakaoUserInfo(456L);
        Member member = new Member(456L, OauthProvider.PROVIDER_KAKAO);

        when(restTemplate.exchange(any(RequestEntity.class), eq(KakaoLoginResponse.class)))
                .thenReturn(ResponseEntity.ok(loginResponse));
        when(restTemplate.exchange(any(RequestEntity.class), eq(KakaoUserInfo.class)))
                .thenReturn(ResponseEntity.ok(userInfo));
        when(memberRepository.findMemberByOauthIdAndOauthProvider(eq(456L), eq(OauthProvider.PROVIDER_KAKAO)))
                .thenReturn(Optional.of(member));
        when(jwtUtil.createToken(member)).thenReturn("mock.jwt.token");

        TokenResponseDto token = kakaoService.kakaoLogin(code);
        assertThat(token.token()).isEqualTo("mock.jwt.token");
    }
}
