package gift.service;

import gift.config.KakaoOauthConfig;
import gift.dto.KakaoLoginResponseDto;
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

    public KakaoLoginResponseDto getAccessToken(String authorizationCode) {
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

        ResponseEntity<KakaoLoginResponseDto> response = restTemplate.exchange(request, KakaoLoginResponseDto.class);

        return response.getBody();
    }
}
