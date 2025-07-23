package gift.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Objects;

@Entity
@Table(name = "options")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotBlank(message = "옵션 이름은 필수 입력 사항입니다.")
    @Size(max = 50, message = "옵션 이름은 최대 50자까지 입력할 수 있습니다.")
    @Pattern(
            regexp = "^[\\p{L}\\p{N} ()\\[\\]+\\-\\&/_]*$",
            message = "옵션 이름은 허용된 특수문자만 사용 가능합니다."
    )
    @Column(nullable = false, length = 50)
    private String name;

    @Min(value = 1, message = "옵션 수량은 최소 1 이상이어야 합니다.")
    @Max(value = 99999999, message = "옵션 수량은 최대 99,999,999까지 입력할 수 있습니다.")
    @Column(nullable = false)
    private int quantity;

    protected Option() {
    }

    public Option(Product product, String name, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("상품은 필수입니다.");
        }
        this.product = product;
        this.name = name;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void subtract(int amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("차감 수량은 1 이상이어야 합니다.");
        }
        if (this.quantity < amount) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        this.quantity -= amount;
    }

    public void update(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Option)) return false;
        Option option = (Option) o;
        return id != null && id.equals(option.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
