package gift.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class OrderTest {
    @Test
    void 주문_수량이_음수인_경우_예외를_던진다() {
        Member member = new Member();
        Option option = new Option();
        assertThatThrownBy(() -> new Order(member, option, -1, "test"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("수량");
    }

    @Test
    void 주문_수량이_1억이_넘을_경우_예외를_던진다() {
        Member member = new Member();
        Option option = new Option();
        assertThatThrownBy(() -> new Order(member, option, 100_000_001, "test"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("수량");
    }
}
