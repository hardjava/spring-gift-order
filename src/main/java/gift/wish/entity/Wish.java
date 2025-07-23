package gift.wish.entity;

import gift.member.entity.Member;
import gift.product.entity.Product;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    protected Wish() {}

    public Wish(Member member, Product product) {
        this.member = member;
        this.product = product;
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

    public boolean isOwner(Long memberId) {
        return Objects.equals(member.getId(), memberId);
    }
}
