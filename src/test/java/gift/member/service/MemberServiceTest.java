package gift.member.service;

import gift.member.dto.MemberLoginRequestDto;
import gift.member.dto.MemberRegisterRequestDto;
import gift.member.dto.TokenResponseDto;
import gift.member.entity.Member;
import gift.member.entity.Role;
import gift.member.exception.EmailAlreadyExistsException;
import gift.member.exception.InvalidPasswordException;
import gift.member.exception.MemberNotFoundException;
import gift.member.repository.MemberRepository;
import gift.security.config.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    private MemberRepository memberRepository;
    private JwtProvider jwtProvider;
    private MemberServiceImpl memberService;

    @BeforeEach
    void setUp() {
        memberRepository = mock(MemberRepository.class);
        jwtProvider = mock(JwtProvider.class);
        memberService = new MemberServiceImpl(memberRepository, jwtProvider);
    }

    @Test
    @DisplayName("회원 가입 성공 시, 저장 및 토큰을 발급한다.")
    void shouldRegisterSuccessfully() {
        // given
        MemberRegisterRequestDto dto = createRegisterDto();
        Member saved = createMember();

        when(memberRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenReturn(saved);
        when(jwtProvider.generateToken(saved)).thenReturn("1234");

        // when
        TokenResponseDto response = memberService.register(dto);

        // then
        assertThat(response.token()).isEqualTo("1234");
        verify(memberRepository, atLeast(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 회원 가입 시 예외를 발생시킨다.")
    void shouldThrowIfEmailAlreadyExists() {
        // given
        var dto = createRegisterDto();
        when(memberRepository.findByEmail(dto.email()))
                .thenReturn(Optional.of(createMember()));

        // when & then
        assertThatThrownBy(() -> memberService.register(dto))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    @DisplayName("올바른 이메일과 비밀번호로 로그인 시 토큰을 반환한다.")
    void shouldLoginSuccessfully() {
        // given
        var dto = new MemberLoginRequestDto("psh@test.com", "1234");
        var member = createMember();
        when(memberRepository.findByEmail(dto.email())).thenReturn(Optional.of(member));
        when(jwtProvider.generateToken(member)).thenReturn("token");

        // when
        TokenResponseDto response = memberService.login(dto);

        // then
        assertThat(response).isEqualTo(new TokenResponseDto("token"));
    }

    @Test
    @DisplayName("비밀번호가 틀릴 경우 로그인 시 예외를 발생시킨다.")
    void shouldThrowIfPasswordIncorrect() {
        // given
        var dto = new MemberLoginRequestDto("psh@test.com", "wrong_pw");
        var member = createMember(); // password = "1234"
        when(memberRepository.findByEmail(dto.email())).thenReturn(Optional.of(member));

        // when & then
        assertThatThrownBy(() -> memberService.login(dto))
                .isInstanceOf(InvalidPasswordException.class);
    }

    @Test
    @DisplayName("회원 삭제 시, 존재하지 않는 ID면 예외가 발생한다.")
    void shouldThrowIfDeletingNonExistentMember() {
        // given
        when(memberRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberService.deleteMember(999L))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("회원 삭제 성공 시 delete가 정상 호출된다.")
    void shouldDeleteMemberSuccessfully() {
        // given
        var member = createMember();
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        // when
        memberService.deleteMember(1L);

        // then
        verify(memberRepository).delete(member);
    }

    private MemberRegisterRequestDto createRegisterDto() {
        return new MemberRegisterRequestDto("솨야", "psh@test.com", "1234");
    }

    private Member createMember() {
        return new Member(1L, "솨야", "psh@test.com", "1234", Role.USER);
    }
}
