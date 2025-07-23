package com.example.demo.service.wish;

import com.example.demo.dto.wish.WishPagingResponseDto;
import com.example.demo.dto.wish.WishResponseDto;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.entity.Wish;
import com.example.demo.entity.WishId;
import com.example.demo.exception.DuplicateWishException;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WishServiceImpl implements WishService{

  private final WishRepository wishRepository;
  private final ProductRepository productRepository;

  public WishServiceImpl(WishRepository wishRepository, ProductRepository productRepository) {
    this.wishRepository = wishRepository;
    this.productRepository = productRepository;
  }

  @Override
  @Transactional
  public void saveWishProduct(User user, Long productId) {
    if (wishRepository.existsById(new WishId(user.getId(), productId))) {
      throw new DuplicateWishException("이미 찜한 상품입니다.");
    }
    Product product = productRepository.findById(productId)
                                       .orElseThrow(() -> new ProductNotFoundException("해당 상품이 존재하지 않습니다."));

    Wish wish = new Wish(new WishId(user.getId(), productId), user, product);
    wishRepository.save(wish);
  }

  @Override
  @Transactional
  public void deleteWishProduct(Long userId, Long productId) {
    WishId wishId = new WishId(userId, productId);
    wishRepository.deleteById(wishId);
  }

  @Override
  public WishPagingResponseDto getWishList(Long userId, int page, int size) {
    Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    Page<WishResponseDto> result = wishRepository.findAllByUserId(userId, pageable)
                                                 .map(WishResponseDto::from);
    return WishPagingResponseDto.from(result);
  }
}
