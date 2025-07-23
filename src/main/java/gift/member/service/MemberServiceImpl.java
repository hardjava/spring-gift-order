package gift.member.service;

import gift.member.dto.*;
import gift.member.entity.Member;
import gift.member.entity.Role;
import gift.member.exception.EmailAlreadyExistsException;
import gift.member.exception.InvalidPasswordException;
import gift.member.exception.MemberNotFoundException;
import gift.member.repository.MemberRepository;
import gift.security.config.JwtProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public MemberServiceImpl(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    @Transactional
    public TokenResponseDto register(MemberRegisterRequestDto memberRegisterRequestDto) {
        String email = memberRegisterRequestDto.email();
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException(email);
        }

        String rawPassword = memberRegisterRequestDto.password();
        Member member = new Member(
                null,
                memberRegisterRequestDto.name(),
                memberRegisterRequestDto.email(),
                rawPassword,
                Role.USER
        );
        Member saved = memberRepository.save(member);

        String token = jwtProvider.generateToken(saved);
        return new TokenResponseDto(token);
    }

    @Override
    @Transactional(readOnly = true)
    public TokenResponseDto login(MemberLoginRequestDto memberLoginRequestDto) {
        Member member = memberRepository.findByEmail(memberLoginRequestDto.email())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!member.isPasswordMatch(memberLoginRequestDto.password())) {
            throw new InvalidPasswordException();
        }

        String token = jwtProvider.generateToken(member);

        return new TokenResponseDto(token);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemberResponseDto> findAllMembers(Pageable pageable) {
        return memberRepository.findAll(pageable)
                .map(member -> new MemberResponseDto(
                        member.getId(),
                        member.getName(),
                        member.getEmail(),
                        member.getRole().name()
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public MemberResponseDto findMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
        return new MemberResponseDto(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getRole().name()
        );
    }

    @Override
    @Transactional
    public void updateMember(Long id, MemberUpdateRequestDto memberUpdateRequestDto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
        member.update(memberUpdateRequestDto.name(), memberUpdateRequestDto.email(), memberUpdateRequestDto.password());
    }

    @Override
    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
        memberRepository.delete(member);
    }
}
