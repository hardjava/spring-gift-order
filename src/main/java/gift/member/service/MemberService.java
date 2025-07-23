package gift.member.service;

import gift.member.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {
    TokenResponseDto register(MemberRegisterRequestDto memberRegisterRequestDto);
    TokenResponseDto login(MemberLoginRequestDto memberLoginRequestDto);
    Page<MemberResponseDto> findAllMembers(Pageable pageable);
    MemberResponseDto findMemberById(Long id);
    void updateMember(Long id, MemberUpdateRequestDto memberUpdateRequestDto);
    void deleteMember(Long id);
}
