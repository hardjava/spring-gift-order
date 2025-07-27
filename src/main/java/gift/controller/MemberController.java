package gift.controller;

import gift.auth.LoginMember;
import gift.domain.Member;
import gift.dto.LoginRequestDto;
import gift.dto.RegisterMemberRequestDto;
import gift.dto.TokenResponseDto;
import gift.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<Void> registerMember(@Valid @RequestBody RegisterMemberRequestDto requestDto) {
        memberService.registerMember(requestDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto) {
        TokenResponseDto responseDto = memberService.login(requestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@LoginMember Member member) {
        memberService.logout(member);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
