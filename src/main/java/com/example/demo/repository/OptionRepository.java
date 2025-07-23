package com.example.demo.repository;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductOption;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<ProductOption, Long> {
  List<ProductOption> findByProductId(Long productId);
  boolean existsByProductAndOptionName(Product product, String optionName);
}
