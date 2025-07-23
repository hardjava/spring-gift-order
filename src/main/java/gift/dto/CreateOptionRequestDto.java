package gift.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateOptionRequestDto(
        @NotBlank(message = "옵션 명을 입력해주세요")
        @Size(max = 50, message = "최대 50자 까지 입력 가능합니다")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣()\\[\\]+\\-&/_ ]*$",
                message = "( ), [ ], +, -, &, /, _ 외의 특수 문자는 사용 불가능 합니다")
        String name,
        @NotNull(message = "수량을 입력해주세요")
        @Positive(message = "수량은 음수가 될 수 없습니다")
        @Max(value = 100_000_000, message = "수량은 1억개를 넘을 수 없습니다")
        @Min(value = 1, message = "수량은 1개 이상이어야 합니다")
        Long quantity
) {

}
