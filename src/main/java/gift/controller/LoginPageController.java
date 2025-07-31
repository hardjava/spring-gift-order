package gift.controller;

import gift.service.KakaoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/login")
public class LoginPageController {
    private final KakaoService kakaoService;

    public LoginPageController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @GetMapping("/page")
    public String loginPage(Model model) {
        model.addAttribute("kakaoLoginUrl", kakaoService.getKakaoLoginUri());

        return "login-page";
    }
}
