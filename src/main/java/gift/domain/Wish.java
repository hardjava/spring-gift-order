package gift.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "wish")
public class Wish extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public Long getId() {
        return id;
    }

    protected Wish() {

    }

    public Wish(Member member, Product product) {
        this.member = member;
        this.product = product;
    }
}
