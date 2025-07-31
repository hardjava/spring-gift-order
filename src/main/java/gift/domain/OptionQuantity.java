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

    public void subtract(int amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("감소시킬 수량은 1 이상이어야 합니다.");
        }

        if (quantity < amount) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }

        this.quantity -= amount;
    }
}
