package gift.service;

import gift.dto.CreateWishRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.WishPageDto;
import gift.dto.WishResponseDto;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.misc.Pair;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishServiceImpl implements WishService {

    private final WishRepository wishRepository;

    private final ProductRepository productRepository;

    private final MemberRepository memberRepository;

    public WishServiceImpl(WishRepository wishRepository,
            ProductRepository productRepository,
            MemberRepository memberRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public WishResponseDto createWish(CreateWishRequestDto requestDto, Long memberId) {
        checkDuplicateWish(requestDto.productId(), memberId);

        Product product = productRepository.findById(requestDto.productId())
                .orElseThrow(() -> new CustomException(ErrorCode.ProductNotfound));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NotRegisterd));

        Wish newWish = new Wish(product, member, requestDto.quantity());

        ProductResponseDto productResponseDto = new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl());

        wishRepository.save(newWish);
        return new WishResponseDto(productResponseDto, requestDto.quantity());
    }

    @Override
    public WishPageDto findMemberWishes(Long memberId, Pageable pageable) {
        Page<Wish> wishes = wishRepository.findAllByMember_Id(memberId, pageable);
        Page<WishResponseDto> responseDtos = toResponseDtoPage(wishes);
        return new WishPageDto(responseDtos);
    }

    @Override
    @Transactional
    public WishResponseDto updateMemberWishQuantityByProductId(
            Long quantity,
            Long productId,
            Long memberId) {
        Wish wish = findMemberWishByProductIdOrElseThrow(productId, memberId);
        wish.changeQuantity(quantity);
        Wish updated = findMemberWishByProductIdOrElseThrow(productId, memberId);
        Product product = updated.getProduct();
        ProductResponseDto productResponseDto = new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl());
        return new WishResponseDto(productResponseDto, updated.getQuantity());
    }

    @Override
    @Transactional
    public void deleteMemberWishByProductId(Long productId, Long memberId) {
        findMemberWishByProductIdOrElseThrow(productId, memberId);
        wishRepository.deleteByProduct_IdAndMember_Id(productId, memberId);
    }

    private void checkDuplicateWish(Long productId, Long memberId) {
        wishRepository.findByProduct_IdAndMember_Id(productId, memberId)
                .ifPresent(wish -> {
                    throw new CustomException(ErrorCode.AlreadyMadeWish);
                });
    }

    private Wish findMemberWishByProductIdOrElseThrow(Long productId, Long memberId) {
        return wishRepository.findByProduct_IdAndMember_Id(productId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.WishNotfound));
    }

    private Page<WishResponseDto> toResponseDtoPage(Page<Wish> page) {
        return page.map(wish -> {
            Product product = wish.getProduct();
            ProductResponseDto responseDto = new ProductResponseDto(product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getImageUrl());
            return new WishResponseDto(responseDto, wish.getQuantity());
        });
    }
}