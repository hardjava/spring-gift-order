package gift.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private Option option;

    @Embedded
    private OptionQuantity optionQuantity;

    private String message;

    protected Order() {

    }

    public Order(Member member, Option option, int quantity, String message) {
        this.member = member;
        this.option = option;
        this.optionQuantity = new OptionQuantity(quantity);
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Long getOptionId() {
        return option.getId();
    }

    public Integer getQuantity() {
        return optionQuantity.getQuantity();
    }

    public String getMessage() {
        return message;
    }
}
