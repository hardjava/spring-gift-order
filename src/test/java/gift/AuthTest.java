package gift;

import gift.component.BCryptEncryptor;
import gift.component.JwtUtil;
import gift.dto.LoginRequestDto;
import gift.dto.TokenResponseDto;
import gift.domain.Member;
import gift.enums.Role;
import gift.exception.UnauthorizedException;
import gift.repository.MemberRepository;
import gift.service.MemberService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
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

    private JwtUtil jwtUtil;

    private final String secretKey = "mysecretkeymysecretkey1234567890";

    @BeforeEach
    void setUp() throws Exception {
        Field secretKeyField = MemberService.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(memberService, secretKey);

        jwtUtil = new JwtUtil();
        Field jwtSecretKeyField = JwtUtil.class.getDeclaredField("secretKey");
        jwtSecretKeyField.setAccessible(true);
        jwtSecretKeyField.set(jwtUtil, secretKey);
    }

    @Test
    void 로그인이_성공하면_토큰이_반환된다() throws NoSuchFieldException, IllegalAccessException {
        LoginRequestDto request = new LoginRequestDto("email@test.com", "1234");
        Member member = new Member("email@test.com", "encoded123", Role.ROLE_USER);
        Field idField = Member.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(member, 1L);
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

    @Test
    void 유효하지_않은_토큰이면_401을_반환한다() {
        String header = "Bearer invalid.token.value";

        assertThatThrownBy(() -> jwtUtil.validateAuthorizationHeader(header, "products-api"))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("유효하지 않은 토큰");
    }

    @Test
    void 권한이_없는_토큰이면_403을_반환한다() {
        String token = Jwts.builder()
                .claim("role", Role.ROLE_USER.name())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();

        String header = "Bearer " + token;

        assertThatThrownBy(() -> jwtUtil.validateAuthorizationAdminHeader(header, "admin-api"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("접근 권한이 없습니다");
    }
}
