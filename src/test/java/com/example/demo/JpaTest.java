package com.example.demo;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.entity.Wish;
import com.example.demo.entity.WishId;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WishRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class JpaTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private WishRepository wishRepository;

  @Test
  void 유저_추가(){
    User user = new User("test@example.com", "abcd1234!", "USERS");
    User savedUser = userRepository.save(user);

    assertAll(
        () -> assertThat(savedUser.getId()).isNotNull(),
        () -> assertThat(savedUser.getEmail()).isEqualTo("test@example.com"),
        () -> assertThat(savedUser.getRole()).isEqualTo("USERS")
    );
  }

  @Test
  void 상품_추가(){
    Product product = new Product("카카오테크캠퍼스", 100, "test.com");
    Product savedProduct = productRepository.save(product);

    assertAll(
        () -> assertThat(savedProduct.getId()).isNotNull(),
        () -> assertThat(savedProduct.getName()).isEqualTo("카카오테크캠퍼스"),
        () -> assertThat(savedProduct.getPrice()).isEqualTo(100),
        () -> assertThat(savedProduct.getImageUrl()).isEqualTo("test.com")
    );
  }

  @Test
  void 위시리스트_추가(){
    User user = new User("test@example.com", "abcd1234!", "USERS");
    Product product = new Product("카카오테크캠퍼스", 100, "test.com");

    User savedUser = userRepository.save(user);
    Product savedProduct = productRepository.save(product);

    WishId wishId = new WishId(savedUser.getId(), savedProduct.getId());
    Wish wish = new Wish(wishId, savedUser, savedProduct);

    Wish savedWish = wishRepository.save(wish);

    assertAll(
        () -> assertThat(savedWish).isNotNull(),
        () -> assertThat(savedWish.getId()).isEqualTo(wishId),
        () -> assertThat(savedWish.getUser()).isEqualTo(savedUser),
        () -> assertThat(savedWish.getProduct()).isEqualTo(savedProduct),
        () -> {
          Optional<Wish> foundWish = wishRepository.findById(wishId);
          assertThat(foundWish).isPresent();
          assertThat(foundWish.get()).isEqualTo(savedWish);
        }
    );
  }
}
