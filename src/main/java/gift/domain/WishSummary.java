package gift.domain;

public class WishSummary {
    private ProductName name;
    private Long count;

    public WishSummary(String productName, Long count) {
        this.name = new ProductName(productName);
        this.count = count;
    }

    public String getProductName() {
        return name.getName();
    }

    public Long getCount() {
        return count;
    }
}
