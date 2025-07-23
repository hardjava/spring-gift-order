package gift.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ProductTest {

    @Test
    void 중복된_옵션을_추가할_경우_예외를_던진다() {
        Option option1 = new Option("test1", 1);
        Option option2 = new Option("test1", 10);
        Product product = new Product("test", 1200L, "test.url");

        product.addOption(option1);
        assertThatThrownBy(() -> product.addOption(option2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("중복");
    }

    @Test
    void 중복된_옵션리스트로_상품을_생성할_경우_예외를_던진다() {
        List<Option> options = List.of(
                new Option("test1", 1),
                new Option("test1", 10)
        );

        assertThatThrownBy(() -> new Product("test", 1200L, "test", options))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("중복");
    }

    @Test
    void 중복되지_않은_옵션리스트로_상품을_생성할_수_있다() {
        List<Option> options = List.of(
                new Option("test1", 1),
                new Option("test2", 10)
        );

        assertThatCode(() -> new Product("test", 1200L, "test", options))
                .doesNotThrowAnyException();
    }

    @Test
    void 카카오가_포함된_이름은_예외를_던진다() {
        assertThatThrownBy(() -> new Product("[카카오] 메로나", 1200L, "img.jpg"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("카카오");
    }

    @Test
    void 허용되지_않은_문자가_포함된_이름은_예외를_던진다() {
        assertThatThrownBy(() -> new Product("메로나!", 1000L, "img.jpg"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("특수 문자");
    }

    @Test
    void 상품_가격이_음수인_경우_예외를_던진다() {
        assertThatThrownBy(() -> new Product("메로나", -12L, "img.jpg"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격");
    }
}
