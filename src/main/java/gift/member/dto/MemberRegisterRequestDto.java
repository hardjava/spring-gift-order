package gift.member.dto;

public record MemberRegisterRequestDto(
        String name,
        String email,
        String password
) {}
