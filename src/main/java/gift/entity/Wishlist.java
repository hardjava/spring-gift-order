package gift.entity;

import jakarta.persistence.*;

@Entity
@Table
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)

    private int quantity;

    public Wishlist() {}


    // 생성자: member, product 객체 받아서 wishlist 생성
    public Wishlist(Member member, Product product) {
        this.member = member;
        this.product = product;
        this.quantity = 1;
    }

    public Wishlist(Long id, Member member, Product product, int quantity) {
        this.id = id;
        this.member = member;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }


    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;

    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public void increaseQuantity() {
        this.quantity += 1;
    }

}
