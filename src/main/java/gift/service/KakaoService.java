package gift.service;

import gift.config.KakaoOauthConfig;
import gift.domain.KakaoLoginResponse;
import gift.domain.KakaoUserInfo;
import gift.dto.TokenResponseDto;
import gift.exception.RestTemplateResponseErrorHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class KakaoService {
    private final KakaoOauthConfig kakaoOauthConfig;
    private final RestTemplate restTemplate;

    public KakaoService(KakaoOauthConfig kakaoOauthConfig, RestTemplateBuilder restTemplateBuilder) {
        this.kakaoOauthConfig = kakaoOauthConfig;
        restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    public TokenResponseDto kakaoLogin(String authorizationCode) {
        KakaoLoginResponse kakaoLoginResponse = getLoginResponse(authorizationCode);
        KakaoUserInfo userInfo = getUserInfo(kakaoLoginResponse.accessToken());

        System.out.println(userInfo.id());

        return null;
    }

    private KakaoLoginResponse getLoginResponse(String authorizationCode) {
        String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoOauthConfig.getClientId());
        body.add("redirect_uri", kakaoOauthConfig.getRedirectURI());
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
