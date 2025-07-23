package gift.service;

import gift.dto.ProductResponseDto;
import gift.dto.WishlistProductDto;

import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wishlist;
import gift.repository.WishlistRepository;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductService productService;
    private final MemberService memberService;

    public WishlistService(WishlistRepository wishlistRepository,
            ProductService productService,
            MemberService memberService) {
        this.wishlistRepository = wishlistRepository;
        this.productService = productService;
        this.memberService = memberService;
    }

    public Page<WishlistProductDto> getWishlist(Long memberId, Pageable pageable) {
        Member member = memberService.findById(memberId);
        Page<Wishlist> wishlistPage = wishlistRepository.findByMember(member, pageable);

        return wishlistPage.map(wishlist -> {
            Product p = wishlist.getProduct();
            if (p == null) return null;
            return new WishlistProductDto(
                    p.getId(),
                    p.getName(),
                    p.getPrice(),
                    p.getImageUrl(),
                    wishlist.getQuantity()
            );
        }).map(Objects::requireNonNull);
    }



    public void addToWishlist(Long memberId, Long productId) {
        Member member = memberService.findById(memberId);
        Product product = productService.findProductEntity(productId);

        wishlistRepository.findByMemberAndProduct(member, product)
                .ifPresentOrElse(
                        Wishlist::increaseQuantity,
                        () -> wishlistRepository.save(new Wishlist(member, product))
                );
    }

    public void removeFromWishlist(Long memberId, Long productId) {
        Member member = memberService.findById(memberId);
        Product product = productService.findProductEntity(productId);

        wishlistRepository.findByMemberAndProduct(member, product)
                .ifPresent(wishlistRepository::delete);
    }
}

