package gift.domain;

import java.time.LocalDateTime;

public record OrderInfo(
        Member member,
        String optionName,
        int quantity,
        String message,
        LocalDateTime paymentTime
) {
    public static OrderInfo from(Order order) {
        return new OrderInfo(
                order.getMember(),
                order.getOptionName(),
                order.getQuantity(),
                order.getMessage(),
                order.getCreatedAt()
        );
    }
}
