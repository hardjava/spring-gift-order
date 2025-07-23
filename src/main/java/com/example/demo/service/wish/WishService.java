package com.example.demo.service.wish;

import com.example.demo.dto.wish.WishPagingResponseDto;
import com.example.demo.entity.User;

public interface WishService {

  void saveWishProduct(User user, Long productId);
  void deleteWishProduct(Long userId, Long productId);
  WishPagingResponseDto getWishList(Long userId, int page, int size);

}
