package gift.controller;


import gift.dto.ProductResponseDto;
import gift.dto.WishlistProductDto;
import gift.dto.WishlistRequestDto;
import gift.entity.Member;
import gift.entity.Product;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import gift.security.LoginMember;
import gift.service.WishlistService;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping

    public ResponseEntity<Page<WishlistProductDto>> getWishlist(
            @LoginMember Member member,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(wishlistService.getWishlist(member.getId(), pageable));

    }


    @PostMapping
    public ResponseEntity<Void> addToWishlist(@LoginMember Member member,
            @RequestBody WishlistRequestDto request) {
        wishlistService.addToWishlist(member.getId(), request.productId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFromWishlist(@LoginMember Member member,
            @PathVariable Long productId) {
        wishlistService.removeFromWishlist(member.getId(), productId);
        return ResponseEntity.noContent().build();
    }
}




