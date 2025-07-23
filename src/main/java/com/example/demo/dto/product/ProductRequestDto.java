package com.example.demo.dto.product;

import com.example.demo.validation.NoForbiddenWords;
import com.example.demo.validation.ValidationConstants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProductRequestDto {

  private Long id;

  @NotBlank(message = ValidationConstants.PRODUCT_NAME_NOT_BLANK)
  @Size(max = 15, message = ValidationConstants.PRODUCT_NAME_SIZE)
  @Pattern(
      regexp = ValidationConstants.NAME_REGEX,
      message = ValidationConstants.NAME_PATTERN_MESSAGE
  )
  @NoForbiddenWords
  private String name;

  @Min(value = 0, message = "상품 가격은 0원 이상이어야 합니다.")
  private int price;

  @NotBlank(message = "이미지 URL은 비어 있을 수 없습니다.")
  private String imageUrl;

  public ProductRequestDto(){
  }

  public ProductRequestDto(Long id, String name, int price, String imageUrl){
    this.id = id;
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
  }

  public ProductRequestDto(String name, int price, String imageUrl){
    this(null, name, price, imageUrl);
  }

  public Long getId(){
    return id;
  }

  public void setId(Long id){
    this.id = id;
  }

  public String getName(){
    return name;
  }

  public int getPrice(){
    return price;
  }

  public String getImageUrl(){
    return imageUrl;
  }

  public void setName(String name) {this.name = name;}

  public void setPrice(int price) {this.price = price;}

  public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}
}
