package gift.service;

import gift.dto.CreateMemberRequestDto;
import gift.dto.DeleteMemberRequestDto;
import gift.dto.JWTResponseDto;
import gift.dto.UpdateMemberPasswordRequestDto;
import gift.entity.Member;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final TokenService tokenService;

    public MemberServiceImpl(MemberRepository memberRepository, TokenService tokenService) {
        this.memberRepository = memberRepository;
        this.tokenService = tokenService;
    }

    @Override
    @Transactional
    public JWTResponseDto createMember(CreateMemberRequestDto requestDto) {
        throwIfMemberFindByEmail(requestDto.email());
        Member newMember = new Member(requestDto.email(), requestDto.password(), "user");
        Member savedMember = memberRepository.save(newMember);
        String accessToken = tokenService.createAccessToken(savedMember);
        return new JWTResponseDto(accessToken);
    }

    @Override
    public JWTResponseDto loginMember(CreateMemberRequestDto requestDto) {
        Member member = findMemberByEmailOrElseThrow(requestDto.email());

        throwIfPasswordIncorrect(member, requestDto.password());

        String accessToken = tokenService.createAccessToken(member);
        return new JWTResponseDto(accessToken);
    }

    @Override
    @Transactional
    public void updateMemberPassword(UpdateMemberPasswordRequestDto requestDto) {
        Member member = findMemberByEmailOrElseThrow(requestDto.email());
        throwIfPasswordIncorrect(member, requestDto.oldPassword());
        member.changePassword(requestDto.newPassword());
    }

    @Override
    @Transactional
    public void deleteMember(DeleteMemberRequestDto requestDto) {
        Member member = findMemberByEmailOrElseThrow(requestDto.email());

        throwIfPasswordIncorrect(member, requestDto.password());

        memberRepository.deleteById(member.getId());
    }

    private Member findMemberByEmailOrElseThrow(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NotRegisterd));
    }

    private void throwIfMemberFindByEmail(String email) {
        memberRepository.findByEmail(email)
                .ifPresent(member -> {
                    throw new CustomException(ErrorCode.AlreadyRegistered);
                });
    }

    private void throwIfPasswordIncorrect(Member member, String password) {
        if (!member.getPassword().equals(password)) {
            throw new CustomException(ErrorCode.Unauthorized);
        }
    }
}
