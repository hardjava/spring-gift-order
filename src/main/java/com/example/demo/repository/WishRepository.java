package com.example.demo.repository;

import com.example.demo.entity.Wish;
import com.example.demo.entity.WishId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, WishId> {

  @EntityGraph(attributePaths = {"product"})
  Page<Wish> findAllByUserId(Long userId, Pageable pageable);
}
