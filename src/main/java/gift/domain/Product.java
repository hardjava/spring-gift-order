package gift.domain;

import gift.dto.CreateProductRequestDto;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "product")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ProductName name;

    @Embedded
    private ProductPrice price;

    @Column(nullable = false)
    private String imageUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Option> options = new HashSet<>();

    protected Product() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public Long getPrice() {
        return price.getPrice();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Set<Option> getOptions() {
        return options;
    }

    public Product(String name, Long price, String imageUrl) {
        this.name = new ProductName(name);
        this.price = new ProductPrice(price);
        this.imageUrl = imageUrl;
    }

    public Product(String name, Long price, String imageUrl, List<Option> options) {
        this(name, price, imageUrl);

        if (options == null || options.isEmpty()) {
            throw new IllegalArgumentException("상품에는 항상 하나 이상의 옵션이 있어야 합니다.");
        }

        for (Option option : options) {
            addOption(option);
        }

    }

    public static Product from(CreateProductRequestDto dto) {
        List<Option> options = dto.options().stream()
                .map(createDto -> new Option(createDto.name(), createDto.quantity()))
                .toList();

        return new Product(
                dto.name(),
                dto.price(),
                dto.imageUrl(),
                options
        );
    }

    public void update(String name, Long price, String imageUrl) {
        this.name = new ProductName(name);
        this.price = new ProductPrice(price);
        this.imageUrl = imageUrl;
    }

    public void addOption(Option option) {
        if (options.contains(option)) {
            throw new IllegalArgumentException("중복된 옵션 이름입니다: " + option.getName());
        }
        options.add(option);
        option.setProduct(this);
    }
}
