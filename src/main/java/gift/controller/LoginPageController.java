package gift.controller;

import gift.service.KakaoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginPageController {
    private final KakaoService kakaoService;

    public LoginPageController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @GetMapping("/")
    public String loginPage(Model model) {
        model.addAttribute("kakaoLoginUrl", kakaoService.getKakaoLoginUri());

        return "login-page";
    }
}
