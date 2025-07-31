package gift.component;

import gift.domain.KakaoMessageTemplateRequest;
import gift.domain.OrderInfo;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KakaoMessageTemplateFactory {

    public KakaoMessageTemplateRequest createOrderTemplate(OrderInfo orderInfo) {
        String textMessage = String.format("""
                [상품을 구매하셨습니다!]
                - 구매처: spring-gift
                - 상품명: %s
                - 수량: %d
                - 메시지: %s
                - 결제일시: %s
                """, orderInfo.optionName(), orderInfo.quantity(), orderInfo.message(), orderInfo.paymentTime());

        return new KakaoMessageTemplateRequest(
                "text",
                textMessage,
                Map.of(
                        "web_url", "http://localhost:8080",
                        "mobile_web_url", "http://localhost:8080"
                ),
                "확인"
        );
    }
}
