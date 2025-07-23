package gift.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class OptionTest {
    @Test
    void 옵션_이름이_비어있을_경우_예외를_던진다() {
        assertThatThrownBy(() -> new Option(" ", 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름");
    }


    @ValueSource(ints = {51, 52, 100})
    @ParameterizedTest
    void 옵션_이름이_50자를_넘을_경우_예외를_던진다(int count) {

        String finalName = "a".repeat(count);
        assertThatThrownBy(() -> new Option(finalName, 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름");
    }

    @Test
    void 옵션_이름에_허용되지_않는_특수문자가_존재할_경우_예외를_던진다() {
        String name = "히.히.히";
        assertThatThrownBy(() -> new Option(name, 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("특수 문자");
    }

    @Test
    void 옵션_수량이_1억이_넘을_경우_예외를_던진다() {
        assertThatThrownBy(() -> new Option("test", 100000001))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("수량");
    }

    @Test
    void 재고가_부족할_경우_예외를_던진다() {
        Option option = new Option("test", 10);
        assertThatThrownBy(() -> option.subtract(15))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("재고");
    }
}
