package gift.service;

import gift.component.BCryptEncryptor;
import gift.component.JwtUtil;
import gift.dto.LoginRequestDto;
import gift.dto.RegisterMemberRequestDto;
import gift.dto.TokenResponseDto;
import gift.domain.Member;
import gift.enums.Role;
import gift.repository.MemberRepository;
import gift.repository.OauthTokenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final OauthTokenRepository oauthTokenRepository;
    private final BCryptEncryptor bCryptEncryptor;
    private final JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, OauthTokenRepository oauthTokenRepository, BCryptEncryptor bCryptEncryptor, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.oauthTokenRepository = oauthTokenRepository;
        this.bCryptEncryptor = bCryptEncryptor;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public void registerMember(RegisterMemberRequestDto requestDto) {
        if (memberRepository.existsMemberByEmail(requestDto.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 이메일 입니다.");
        }

        memberRepository.save(
                new Member(
                        requestDto.email(),
                        bCryptEncryptor.encode(requestDto.password()),
                        Role.ROLE_USER
                )
        );
    }

    @Transactional
    public TokenResponseDto login(LoginRequestDto requestDto) {
        Optional<Member> findMember = memberRepository.findMemberByEmail(requestDto.email());
        if (findMember.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 불일치 합니다.");
        }

        Member member = findMember.get();
        if (!bCryptEncryptor.isMatch(requestDto.password(), member.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 불일치 합니다.");
        }

        return new TokenResponseDto(jwtUtil.createToken(member));
    }

    public Member findMemberById(Long id) {
        Optional<Member> findMember = memberRepository.findMemberById(id);
        if (findMember.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "[id = " + id + "] 해당 id의 회원을 찾을 수 없습니다.");
        }

        return findMember.get();
    }

    @Transactional
    public void logout(Member member) {
        oauthTokenRepository.deleteByMember(member);
    }
}
