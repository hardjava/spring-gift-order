package gift.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptionTest {

    private Product dummyProduct() {
        return new Product(1L, "상품", 1000, "image.jpg");
    }

    @Test
    void subtract_차감기능() {
        Option option = new Option(dummyProduct(), "Option A", 10);
        option.subtract(3);
        assertEquals(7, option.getQuantity());
    }

    @Test
    void subtract_수량이상_차감시_예외던지기() {
        Option option = new Option(dummyProduct(), "Option A", 5);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            option.subtract(6);
        });
        assertEquals("재고가 부족합니다.", exception.getMessage());
    }

    @Test
    void subtract_차감수량은_항상1이상() {
        Option option = new Option(dummyProduct(), "Option A", 5);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            option.subtract(0);
        });
        assertEquals("차감 수량은 1 이상이어야 합니다.", exception.getMessage());
    }

    @Test
    void update_이름과수량변경() {
        Option option = new Option(dummyProduct(), "Old Name", 10);
        option.update("New Name", 20);
        assertEquals("New Name", option.getName());
        assertEquals(20, option.getQuantity());
    }
}
