package com.example.demo.dto.wish;

import com.example.demo.entity.Wish;

public record WishResponseDto(
    Long productId,
    String name,
    int price,
    String imageUrl
) {
  public static WishResponseDto from(Wish wish){
    return new WishResponseDto(
        wish.getProduct().getId(),
        wish.getProduct().getName(),
        wish.getProduct().getPrice(),
        wish.getProduct().getImageUrl()
    );
  }
}
