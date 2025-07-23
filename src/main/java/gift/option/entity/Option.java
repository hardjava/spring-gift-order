package gift.option.entity;

import gift.product.entity.Product;
import jakarta.persistence.*;

@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(nullable = false)
    private int quantity;

    protected Option() {}

    public Option(Product product, String name, int quantity) {
        this.product = product;
        this.name = name;
        this.quantity = quantity;
    }

    public void subtract(int count) {
        if (this.quantity < count) {
            throw new IllegalArgumentException("수량이 부족합니다.");
        }
        this.quantity -= count;
    }

    public void update(String newName, int newQuantity) {
        this.name = newName;
        this.quantity = newQuantity;
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

    public void setProduct(Product product) {
        this.product = product;
    }
}
