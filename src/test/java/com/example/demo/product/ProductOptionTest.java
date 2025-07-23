package com.example.demo.product;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import com.example.demo.dto.product.ProductOptionRequestDto;
import com.example.demo.dto.product.ProductOptionResponseDto;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductOption;
import com.example.demo.repository.OptionRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.product.OptionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class ProductOptionTest {

  @Autowired
  private OptionService optionService;

  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private OptionRepository optionRepository;

  @Test
  void 옵션_정상_저장(){
    Product product = productRepository.save(new Product("테스트 상품", 1000, "image.jpg"));
    ProductOptionRequestDto dto = new ProductOptionRequestDto("TestOption", 1);

    ProductOptionResponseDto saved = optionService.saveOption(product.getId(), dto);

    assertThat(saved.name()).isEqualTo("TestOption");
  }

  @Test
  void 옵션_수량_감소_정상_작동(){
    Product product = productRepository.save(new Product("테스트 상품", 10000, "image.jpg"));
    ProductOption option = optionRepository.save(new ProductOption(product, "TestOption", 100));

    option.subtract(50);
    assertThat(option.getQuantity()).isEqualTo(50);
  }

  @Test
  void 옵션_수량_감소_예외처리_정상_작동(){
    Product product = productRepository.save(new Product("테스트 상품", 10000, "image.jpg"));
    ProductOption option = optionRepository.save(new ProductOption(product, "TestOption", 100));

    assertThatThrownBy(() -> option.subtract(101))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("수량 부족");
  }
}
