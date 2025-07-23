package com.example.demo.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class WishId implements Serializable {

  private Long userId;
  private Long productId;

  protected WishId() {}

  public WishId(Long userId, Long productId){
    this.userId = userId;
    this.productId = productId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof WishId)) return false;
    WishId that = (WishId) o;
    return Objects.equals(userId, that.userId) &&
        Objects.equals(productId, that.productId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, productId);
  }
}
