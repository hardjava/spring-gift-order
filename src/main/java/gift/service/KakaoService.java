package gift.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.component.JwtUtil;
import gift.config.KakaoOauthConfig;
import gift.domain.*;
import gift.dto.TokenResponseDto;
import gift.enums.OauthProvider;
import gift.exception.RestTemplateResponseErrorHandler;
import gift.repository.MemberRepository;
import gift.repository.OauthTokenRepository;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Map;

@Service
public class KakaoService {
    private final MemberRepository memberRepository;
    private final OauthTokenRepository oauthTokenRepository;
    private final KakaoOauthConfig kakaoOauthConfig;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public KakaoService(MemberRepository memberRepository, OauthTokenRepository oauthTokenRepository, KakaoOauthConfig kakaoOauthConfig, RestTemplateBuilder restTemplateBuilder, JwtUtil jwtUtil, ObjectMapper objectMapper) {
        this.memberRepository = memberRepository;
        this.oauthTokenRepository = oauthTokenRepository;
        this.kakaoOauthConfig = kakaoOauthConfig;
        this.jwtUtil = jwtUtil;
        restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
        this.objectMapper = objectMapper;
    }

    @Transactional
    public TokenResponseDto kakaoLogin(String authorizationCode) {
        KakaoLoginResponse kakaoLoginResponse = getLoginResponse(authorizationCode);
        KakaoUserInfo userInfo = getUserInfo(kakaoLoginResponse.accessToken());

        Member member = memberRepository
                .findMemberByOauthIdAndOauthProvider(userInfo.id(), OauthProvider.PROVIDER_KAKAO)
                .orElseGet(() -> createMemberByKaKaoId(userInfo.id()));

        oauthTokenRepository.save(
                new OauthToken(member, kakaoLoginResponse.accessToken(), kakaoLoginResponse.refreshToken())
        );

        return new TokenResponseDto(jwtUtil.createToken(member));
    }

    protected Member createMemberByKaKaoId(Long kakaoId) {
        return memberRepository.save(
                new Member(kakaoId, OauthProvider.PROVIDER_KAKAO)
        );
    }

    private KakaoLoginResponse getLoginResponse(String authorizationCode) {
        String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoOauthConfig.clientId());
        body.add("redirect_uri", kakaoOauthConfig.redirectUri());
        body.add("code", authorizationCode);

        RequestEntity<LinkedMultiValueMap<String, String>> request = new RequestEntity<>(
                body, headers, HttpMethod.POST, URI.create(url)
        );

        ResponseEntity<KakaoLoginResponse> response = restTemplate.exchange(request, KakaoLoginResponse.class);

        return response.getBody();
    }

    private KakaoUserInfo getUserInfo(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        RequestEntity<Void> request = new RequestEntity<>(
                headers, HttpMethod.GET, URI.create(url));

        ResponseEntity<KakaoUserInfo> response = restTemplate.exchange(request, KakaoUserInfo.class);

        return response.getBody();
    }

    public void sendKakaoMessage(OrderInfo orderInfo) {
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        OauthToken accessToken = oauthTokenRepository.findByMemberOrElseThrow(orderInfo.member());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(accessToken.getAccessToken());

        Map<String, Object> templateObject = createTemplateObject(orderInfo);
        try {
            String templateJson = objectMapper.writeValueAsString(templateObject);
            LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("template_object", templateJson);

            RequestEntity<LinkedMultiValueMap<String, String>> request = new RequestEntity<>(
                    body, headers, HttpMethod.POST, URI.create(url)
            );

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 메시지 전송 실패", e);
        }
    }

    private Map<String, Object> createTemplateObject(OrderInfo orderInfo) {
        String textMessage = String.format("""
                [상품을 구매하셨습니다!]
                - 구매처: spring-gift
                - 상품명: %s
                - 수량: %d
                - 메시지: %s
                - 결제일시: %s
                """, orderInfo.optionName(), orderInfo.quantity(), orderInfo.message(), orderInfo.paymentTime());

        Map<String, Object> templateObject = Map.of(
                "object_type", "text",
                "text", textMessage,
                "link", Map.of(
                        "web_url", "http://localhost:8080",
                        "mobile_web_url", "http://localhost:8080"
                ),
                "button_title", "확인"
        );

        return templateObject;
    }
}
