package gift.wish.service;

import gift.member.entity.Member;
import gift.member.exception.MemberNotFoundException;
import gift.member.repository.MemberRepository;
import gift.product.entity.Product;
import gift.product.exception.ProductNotFoundException;
import gift.product.repository.ProductRepository;
import gift.security.exception.AccessDeniedException;
import gift.wish.dto.WishResponseDto;
import gift.wish.entity.Wish;
import gift.wish.exception.DuplicateWishException;
import gift.wish.exception.WishNotFoundException;
import gift.wish.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WishServiceImpl implements WishService {
    private final WishRepository wishRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public WishServiceImpl(WishRepository wishRepository, ProductRepository productRepository, MemberRepository memberRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional
    public WishResponseDto createWish(Long memberId, Long productId) {
        if (wishRepository.existsByMemberIdAndProductId(memberId, productId)) {
            throw new DuplicateWishException(memberId, productId);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Wish savedWish = wishRepository.save(new Wish(member, product));

        return WishResponseDto.of(savedWish);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WishResponseDto> findAllWishesByMemberId(Long memberId, Pageable pageable) {
        Page<Wish> page = wishRepository.findAllByMemberId(memberId, pageable);

        Sort.Order order = pageable.getSort().getOrderFor("name");

        List<WishResponseDto> sortedContent = page.getContent().stream()
                .map(WishResponseDto::of)
                .sorted((a, b) -> {
                    if (order != null && order.isDescending()) {
                        return b.productName().compareTo(a.productName());
                    }
                    return a.productName().compareTo(b.productName());
                })
                .toList();

        return new PageImpl<>(sortedContent, pageable, page.getTotalElements());
    }

    @Override
    @Transactional
    public void deleteWish(Long memberId, Long wishId) {
        Wish wish = wishRepository.findById(wishId)
                .orElseThrow(() -> new WishNotFoundException(wishId));

        if (!wish.isOwner(memberId)) {
            throw new AccessDeniedException(memberId);
        }

        wishRepository.delete(wish);
    }
}
