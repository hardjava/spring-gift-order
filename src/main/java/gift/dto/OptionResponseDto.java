package gift.dto;

import gift.domain.OptionResponse;

import java.util.List;

public record OptionResponseDto(
        List<OptionResponse> options
) {
}
