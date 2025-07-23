package gift.member.controller;

import gift.member.dto.MemberLoginRequestDto;
import gift.member.dto.MemberRegisterRequestDto;
import gift.member.dto.TokenResponseDto;
import gift.member.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthViewController {

    private final MemberService memberService;
    public AuthViewController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "member/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberLoginRequestDto dto, Model model) {
        try {
            TokenResponseDto tokenDto = memberService.login(dto);
            model.addAttribute("token", tokenDto.token());
            return "redirect:/admin/members";
        } catch (Exception e) {
            model.addAttribute("error", "로그인 실패");
            return "member/login";
        }
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("dto", new MemberRegisterRequestDto("", "", ""));
        return "member/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute MemberRegisterRequestDto dto) {
        memberService.register(dto);
        return "redirect:/login";
    }
}

