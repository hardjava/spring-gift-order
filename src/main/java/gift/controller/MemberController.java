package gift.controller;

import gift.dto.CreateMemberRequestDto;
import gift.dto.DeleteMemberRequestDto;
import gift.dto.JWTResponseDto;
import gift.dto.UpdateMemberPasswordRequestDto;
import gift.service.MemberService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<JWTResponseDto> createMember(
            @Valid @RequestBody CreateMemberRequestDto requestDto) {
        return new ResponseEntity<>(memberService.createMember(requestDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JWTResponseDto> loginMember(
            @Valid @RequestBody CreateMemberRequestDto requestDto) {
        return new ResponseEntity<>(memberService.loginMember(requestDto), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<Void> updateMemberPassword(
            @RequestBody UpdateMemberPasswordRequestDto requestDto
    ) {
        memberService.updateMemberPassword(requestDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMember(
            @RequestBody DeleteMemberRequestDto requestDto
    ) {
        memberService.deleteMember(requestDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
