package gift.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record KakaoMessageTemplateRequest(
        @JsonProperty("object_type")
        String objectType,
        String text,
        Map<String, String> link,
        @JsonProperty("button_title")
        String buttonTitle
) {
}
