package gift.member.controller;

import gift.member.dto.*;
import gift.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> register(@RequestBody MemberRegisterRequestDto memberRegisterRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.register(memberRegisterRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {
        return ResponseEntity.ok(memberService.login(memberLoginRequestDto));
    }

    @GetMapping
    public ResponseEntity<Page<MemberResponseDto>> findAllMembers(Pageable pageable) {
        return ResponseEntity.ok(memberService.findAllMembers(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> findMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.findMemberById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMember(@PathVariable Long id, @RequestBody MemberUpdateRequestDto memberUpdateRequestDto) {
        memberService.updateMember(id, memberUpdateRequestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

}
