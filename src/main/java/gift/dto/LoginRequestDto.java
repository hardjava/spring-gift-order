package gift.dto;

import gift.validation.ValidMemberEmail;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @ValidMemberEmail
        String email,

        @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")
        String password
) {
}
