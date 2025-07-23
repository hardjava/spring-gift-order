package gift.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ProductName {
    @Column(name = "name", nullable = false)
    private String name;

    protected ProductName() {

    }

    public ProductName(String name) {
        validateName(name);
        this.name = name;
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("상품 이름은 비어 있을 수 없습니다.");
        }

        if (name.length() > 15) {
            throw new IllegalArgumentException("상품 이름은 공백 포함 최대 15자까지 입력할 수 있습니다.");
        }

        if (!name.matches("^[\\p{L}\\p{N}\\s\\(\\)\\[\\]\\+\\-\\&/_]*$")) {
            throw new IllegalArgumentException("상품 이름에는 ( ), [ ], +, -, &, /, _ 의 특수 문자만 사용할 수 있습니다.");
        }

        if (name.contains("카카오")) {
            throw new IllegalArgumentException("상품 이름에 '카카오'를 포함하려면 MD 승인이 필요합니다.");
        }
    }

    public String getName() {
        return name;
    }
}
