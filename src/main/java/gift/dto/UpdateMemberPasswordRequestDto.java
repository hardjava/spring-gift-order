package gift.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateMemberPasswordRequestDto(
        @Email(message = "이메일을 입력해주세요")
        String email,
        @NotBlank(message = "이전 비밀번호를 입력해주세요")
        String oldPassword,
        @NotBlank(message = "변경할 비밀번호를 입력해주세요")
        String newPassword
) {

}
