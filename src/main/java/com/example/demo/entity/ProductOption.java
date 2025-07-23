package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_option")
public class ProductOption {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @Column(name = "option_name", length = 50, nullable = false)
  private String optionName;

  @Column(nullable = false)
  private int quantity;

  public ProductOption(Product product, String optionName, int quantity){
    if(quantity < 1){
      throw new IllegalArgumentException("수량은 1이상이어야 합니다.");
    }
    this.product = product;
    this.optionName = optionName;
    this.quantity = quantity;
  }

  protected ProductOption() {}

  public void subtract(int amount){
    if(this.quantity == 0){
      throw new IllegalArgumentException("해당 옵션은 품절입니다.");
    }

    if(amount < 1 || amount > this.quantity){
      throw new IllegalArgumentException("수량 부족 및 요청 수량 오류");
    }
    quantity -= amount;
  }

  public Long getId(){
    return id;
  }

  public Product getProduct(){
    return product;
  }
  public String getOptionName(){
    return optionName;
  }

  public int getQuantity(){
    return quantity;
  }
}
