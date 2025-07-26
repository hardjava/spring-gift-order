package gift.service;

import gift.component.JwtUtil;
import gift.config.KakaoOauthConfig;
import gift.domain.KakaoLoginResponse;
import gift.domain.KakaoUserInfo;
import gift.domain.Member;
import gift.dto.TokenResponseDto;
import gift.enums.OauthProvider;
import gift.exception.RestTemplateResponseErrorHandler;
import gift.repository.MemberRepository;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class KakaoService {
    private final MemberRepository memberRepository;
    private final KakaoOauthConfig kakaoOauthConfig;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    public KakaoService(MemberRepository memberRepository, KakaoOauthConfig kakaoOauthConfig, RestTemplateBuilder restTemplateBuilder, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.kakaoOauthConfig = kakaoOauthConfig;
        this.jwtUtil = jwtUtil;
        restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    @Transactional
    public TokenResponseDto kakaoLogin(String authorizationCode) {
        KakaoLoginResponse kakaoLoginResponse = getLoginResponse(authorizationCode);
        KakaoUserInfo userInfo = getUserInfo(kakaoLoginResponse.accessToken());

        Member member = memberRepository
                .findMEmberByKakaoIdAndOauthProvider(userInfo.id(), OauthProvider.PROVIDER_KAKAO)
                .orElseGet(() -> createMemberByKaKaoId(userInfo.id()));

        return new TokenResponseDto(jwtUtil.createToken(member));
    }

    protected Member createMemberByKaKaoId(Long kakoId) {
        return memberRepository.save(
                new Member(kakoId, OauthProvider.PROVIDER_KAKAO)
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
}
