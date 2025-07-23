package gift.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ProductPrice {
    @Column(name = "price", nullable = false)
    private Long price;

    protected ProductPrice() {

    }

    public ProductPrice(Long price) {
        validatePrice(price);
        this.price = price;
    }

    public Long getPrice() {
        return price;
    }

    private static void validatePrice(Long price) {
        if (price == null) {
            throw new IllegalArgumentException("가격은 필수 입력입니다.");
        }

        if (price < 0) {
            throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
        }
    }
}
