package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "wish")
public class Wish {

  @EmbeddedId
  private WishId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("productId")
  @JoinColumn(name ="product_id")
  private Product product;

  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column
  private LocalDateTime updatedAt;

  protected Wish(){}

  public Wish(WishId id, User user, Product product) {
    this.id = id;
    this.user = user;
    this.product = product;
  }

  public WishId getId(){
    return id;
  }

  public User getUser(){
    return user;
  }

  public Product getProduct(){
    return product;
  }

  public LocalDateTime getCreatedAt(){
    return createdAt;
  }

  public LocalDateTime getUpdatedAt(){
    return updatedAt;
  }
}
