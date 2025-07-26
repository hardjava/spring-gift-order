package gift.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class OptionQuantity {
    @Column(name = "quantity")
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    protected OptionQuantity() {

    }

    public OptionQuantity(int quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    private static void validateQuantity(int quantity) {
        if (quantity < 1 || quantity > 100000000) {
            throw new IllegalArgumentException("옵션 수량은 최소 1개 이상 1억 개 미만입니다.");
        }
    }
}
