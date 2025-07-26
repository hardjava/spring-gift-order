package gift;

import gift.component.BCryptEncryptor;
import gift.component.JwtUtil;
import gift.dto.LoginRequestDto;
import gift.dto.TokenResponseDto;
import gift.domain.Member;
import gift.enums.Role;
import gift.repository.MemberRepository;
import gift.service.MemberService;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AuthTest {
    @Mock
    MemberRepository memberRepository;

    @Mock
    BCryptEncryptor bCryptEncryptor;

    @InjectMocks
    MemberService memberService;

    @Mock
    JwtUtil jwtUtil;

    @Test
    void 로그인이_성공하면_토큰이_반환된다() {
        LoginRequestDto request = new LoginRequestDto("email@test.com", "1234");
        Member member = new Member("email@test.com", "encoded123", Role.ROLE_USER);
        given(jwtUtil.createToken(any())).willReturn("mock-token");

        // given
        given(memberRepository.findMemberByEmail(any())).willReturn(Optional.of(member));
        given(bCryptEncryptor.isMatch(any(), any())).willReturn(true);

        // when
        TokenResponseDto response = memberService.login(request);

        // then
        assertThat(response.token()).isNotBlank();
    }

    @Test
    void 비밀번호가_틀리면_401을_반환한다() {
        LoginRequestDto requestDto = new LoginRequestDto("email@test.com", "wrongPassword");
        Member member = new Member("email@test.com", "realPassword", Role.ROLE_USER);

        given(memberRepository.findMemberByEmail(any())).willReturn(Optional.of(member));
        given(bCryptEncryptor.isMatch(any(), any())).willReturn(false);

        assertThatThrownBy(() -> memberService.login(requestDto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("401");
    }
}
