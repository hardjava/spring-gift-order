package gift.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "option",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_productId_name", columnNames = {"product_id", "name"})
        }
)
public class Option extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Embedded
    private OptionName name;

    @Embedded
    private OptionQuantity quantity;

    protected Option() {

    }

    public Option(String name, int quantity) {
        this.name = new OptionName(name);
        this.quantity = new OptionQuantity(quantity);
    }

    public void subtract(int amount) {
        quantity.subtract(amount);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public int getQuantity() {
        return quantity.getQuantity();
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Option)) return false;

        Option option = (Option) o;

        return name.getName() != null && name.getName().equals(option.getName());
    }

    @Override
    public int hashCode() {
        return name.getName() != null ? name.getName().hashCode() : 0;
    }
}
