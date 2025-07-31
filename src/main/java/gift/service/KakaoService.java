package gift.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.component.JwtUtil;
import gift.config.KakaoOauthConfig;
import gift.config.RestClientConfig;
import gift.domain.*;
import gift.dto.TokenResponseDto;
import gift.enums.OauthProvider;
import gift.repository.MemberRepository;
import gift.repository.OauthTokenRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class KakaoService {
    private final MemberRepository memberRepository;
    private final OauthTokenRepository oauthTokenRepository;
    private final KakaoOauthConfig kakaoOauthConfig;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final URI kakaoLoginUri;
    private static final String KAKAO_AUTH_BASE_URL = "https://kauth.kakao.com";
    private static final String KAKAO_API_BASE_URL = "https://kapi.kakao.com";
    private final RestClient restClient;

    public KakaoService(MemberRepository memberRepository, OauthTokenRepository oauthTokenRepository, KakaoOauthConfig kakaoOauthConfig, JwtUtil jwtUtil, ObjectMapper objectMapper) {
        this.memberRepository = memberRepository;
        this.oauthTokenRepository = oauthTokenRepository;
        this.kakaoOauthConfig = kakaoOauthConfig;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
        this.kakaoLoginUri = createKakaoUri();
        this.restClient = new RestClientConfig().restClientBuilder().build();
    }

    private URI createKakaoUri() {
        return UriComponentsBuilder.fromUriString(KAKAO_AUTH_BASE_URL)
                .path("/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", kakaoOauthConfig.clientId())
                .queryParam("redirect_uri", kakaoOauthConfig.redirectUri())
                .queryParam("scope", "talk_message")
                .queryParam("prompt", "consent")
                .build().toUri();
    }

    public URI getKakaoLoginUri() {
        return kakaoLoginUri;
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
        String url = KAKAO_AUTH_BASE_URL + "/oauth/token";
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoOauthConfig.clientId());
        body.add("redirect_uri", kakaoOauthConfig.redirectUri());
        body.add("code", authorizationCode);

        return restClient.post()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(body)
                .retrieve()
                .body(KakaoLoginResponse.class);
    }

    private KakaoUserInfo getUserInfo(String accessToken) {
        String url = KAKAO_API_BASE_URL + "/v2/user/me";

        return restClient.get()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
                .retrieve()
                .body(KakaoUserInfo.class);
    }

    public void sendKakaoMessage(Member member, KakaoMessageTemplateRequest templateRequest) {
        String url = KAKAO_API_BASE_URL + "/v2/api/talk/memo/default/send";
        OauthToken accessToken = oauthTokenRepository.findByMemberOrElseThrow(member);

        try {
            String templateJson = objectMapper.writeValueAsString(templateRequest);
            LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("template_object", templateJson);

            String response = restClient.post()
                    .uri(url)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken.getAccessToken()))
                    .body(body)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 메시지 전송 실패", e);
        }
    }
}
