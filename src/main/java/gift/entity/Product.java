package gift.entity;

import gift.dto.CreateProductRequestDto;
import gift.dto.ProductResponseDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @Column(name = "options", nullable = false)
    private List<Option> options;

    protected Product() {

    }

    public Product(String name, Long price, String imageUrl) {
        this(null, name, price, imageUrl, new ArrayList<Option>());
    }

    public Product(String name, Long price, String imageUrl, List<Option> options) {
        this(null, name, price, imageUrl, options);
    }

    public Product(Long id, String name, Long price, String imageUrl, List<Option> options) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.options = options;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<Option> getOptions() {
        return new ArrayList<>(options);
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changePrice(Long price) {
        this.price = price;
    }

    public void changeImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void addOption(Option option){
        options.add(option);
    }
}
