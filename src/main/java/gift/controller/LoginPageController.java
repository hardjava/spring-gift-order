package gift.controller;

import gift.config.KakaoOauthConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Controller
@RequestMapping("/login")
public class LoginPageController {
    private final KakaoOauthConfig kakaoOauthConfig;

    public LoginPageController(KakaoOauthConfig kakaoOauthConfig) {
        this.kakaoOauthConfig = kakaoOauthConfig;
    }

    @GetMapping("/page")
    public String loginPage(Model model) {
        URI uri =
                UriComponentsBuilder.fromUriString("https://kauth.kakao.com")
                        .path("oauth/authorize")
                        .queryParam("response_type", "code")
                        .queryParam("client_id", kakaoOauthConfig.clientId())
                        .queryParam("redirect_uri", kakaoOauthConfig.redirectUri())
                        .build().toUri();

        model.addAttribute("kakaoLoginUrl", uri);
        return "login-page";
    }
}
