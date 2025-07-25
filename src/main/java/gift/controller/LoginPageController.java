package gift.controller;

import gift.config.KakaoOauthConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginPageController {
    private final KakaoOauthConfig kakaoOauthConfig;

    public LoginPageController(KakaoOauthConfig kakaoOauthConfig) {
        this.kakaoOauthConfig = kakaoOauthConfig;
    }

    @GetMapping("/page")
    public String loginPage(Model model) {
        String url = String.format(
                "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=%s&redirect_uri=%s",
                kakaoOauthConfig.getClientId(),
                kakaoOauthConfig.getRedirectURI()
        );

        model.addAttribute("kakaoLoginUrl", url);
        return "login-page";
    }
}
