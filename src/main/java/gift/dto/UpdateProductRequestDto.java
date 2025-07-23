package gift.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;

public record UpdateProductRequestDto(
        @NotBlank(message = "이름을 입력해주세요")
        @Size(max = 15, message = "최대 15자 까지 입력 가능합니다")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣()\\[\\]+\\-&/_ ]*$",
                message = "( ), [ ], +, -, &, /, _ 외의 특수 문자는 사용 불가능 합니다")
        String name,

        @NotNull(message = "가격을 입력해주세요")
        @Positive(message = "가격은 음수가 될 수 없습니다")
        Long price,

        @NotBlank(message = "이미지 Url을 입력해주세요")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣()\\[\\]+\\-&/_ ]*$",
                message = "( ), [ ], +, -, &, /, _ 외의 특수 문자는 사용 불가능 합니다")
        String imageUrl
) {

}
