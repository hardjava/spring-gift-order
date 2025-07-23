package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.CreateWishRequestDto;
import gift.dto.UpdateWishQuantityRequstDto;
import gift.dto.WishPageDto;
import gift.dto.WishResponseDto;
import gift.entity.Member;
import gift.service.WishService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping
    public ResponseEntity<WishResponseDto> createWish(
            @RequestBody CreateWishRequestDto requestDto,
            @LoginMember Member member) {
        Long memberId = member.getId();
        return new ResponseEntity<>(wishService.createWish(requestDto, memberId),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<WishPageDto> findMemberWishes(
            @LoginMember Member member,
            @PageableDefault(size = 5, direction = Direction.ASC) Pageable pageable) {
        Long memberId = member.getId();
        WishPageDto wishes = wishService.findMemberWishes(memberId, pageable);
        return new ResponseEntity<>(wishes, HttpStatus.OK);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<WishResponseDto> updateMemberWishQuantityByProductId(
            @Valid @RequestBody UpdateWishQuantityRequstDto requestDto,
            @PathVariable Long productId,
            @LoginMember Member member) {
        Long memberId = member.getId();
        if (requestDto.quantity().equals(0L)) {
            wishService.deleteMemberWishByProductId(productId, memberId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(
                wishService.updateMemberWishQuantityByProductId(requestDto.quantity(), productId,
                        memberId),
                HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteMemberWishByProductId(
            @PathVariable Long productId,
            @LoginMember Member member) {
        Long memberId = member.getId();
        wishService.deleteMemberWishByProductId(productId, memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}