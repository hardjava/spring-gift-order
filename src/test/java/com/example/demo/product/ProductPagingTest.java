package com.example.demo.product;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductPagingTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ProductRepository productRepository;

  @BeforeEach
  void setUp() {
    LocalDateTime baseTime = LocalDateTime.now();
    for (int i = 1; i <= 105; i++) {
      Product product = new Product("상품 " + i, i * 1000, "https://dummytest.com");
      product.setCreatedAt(baseTime.minusSeconds(105 - i));
      productRepository.save(product);
    }
  }

  @Test
  void 첫번째_페이지_조회시_10개가_반환되고_가장_최신_상품이_첫번째에_위치한다() throws Exception{
    mockMvc.perform(get("/product-page")
        .param("page", "0"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("paging"))
        .andExpect(model().attribute("paging", Matchers.hasProperty("content", Matchers.hasSize(10))))
        .andExpect(model().attribute("paging", Matchers.hasProperty("number", Matchers.equalTo(0))))
        .andExpect(model().attribute("paging", Matchers.hasProperty("totalPages", Matchers.equalTo(11))));
  }

  @Test
  void 페이징_요청시_상품목록_순서가_정확히_반환된다() throws Exception {
    MvcResult result = mockMvc.perform(get("/product-page?page=3&size=10"))
                              .andExpect(status().isOk())
                              .andReturn();

    Page<Product> paging = (Page<Product>)
        result.getModelAndView().getModel().get("paging");

    List<Product> products = paging.getContent();
    assertThat(products.get(2).getName()).isEqualTo("상품 73");
  }
}
