package com.example.demo.dto.wish;

import java.util.List;
import org.springframework.data.domain.Page;

public record WishPagingResponseDto(
    List<WishResponseDto> items,
    int page,
    int size,
    int totalPages,
    long totalElements
) {
  public static WishPagingResponseDto from(Page<WishResponseDto> pageResult){
    return new WishPagingResponseDto(
        pageResult.getContent(),
        pageResult.getNumber() + 1,
        pageResult.getSize(),
        pageResult.getTotalPages(),
        pageResult.getTotalElements()
    );
  }
}
