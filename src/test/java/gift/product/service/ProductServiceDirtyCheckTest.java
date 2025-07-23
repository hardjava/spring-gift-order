package gift.product.service;

import gift.option.dto.OptionCreateRequestDto;
import gift.product.dto.ProductRequestDto;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@SpringBootTest(properties = {
        "spring.sql.init.mode=never",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
class ProductServiceDirtyCheckTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductServiceImpl service;

    @Test
    void updateProduct_appliesDirtyChecking() {
        // given
        Product product = new Product("하리보 젤리(original)", 1500, "http://img.url/original.png");
        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

        // when
        service.updateProduct(
                product.getId(),
                new ProductRequestDto(
                        "하리보 젤리(new)",
                        2000,
                        "http://img.url/new.png",
                        List.of(new OptionCreateRequestDto("날개", 10))
                )
        );

        // then
        assertThat(product.getName()).isEqualTo("하리보 젤리(new)");
        assertThat(product.getPrice()).isEqualTo(2000);
        assertThat(product.getImageUrl()).isEqualTo("http://img.url/new.png");
        then(productRepository).should(never()).save(any());
    }
}

