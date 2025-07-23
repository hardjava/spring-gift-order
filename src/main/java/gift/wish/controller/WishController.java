package gift.wish.controller;

import gift.member.dto.AuthenticatedMemberDto;
import gift.security.annotation.LoginMember;
import gift.wish.dto.WishRequestDto;
import gift.wish.dto.WishResponseDto;
import gift.wish.service.WishService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping
    public ResponseEntity<WishResponseDto> createWish(
            @RequestBody WishRequestDto request,
            @LoginMember AuthenticatedMemberDto authenticatedMemberDto
    ) {
        WishResponseDto created = wishService.createWish(authenticatedMemberDto.id(), request.productId());
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<Page<WishResponseDto>> findAllWish(
            @LoginMember AuthenticatedMemberDto authenticatedMemberDto,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<WishResponseDto> result = wishService.findAllWishesByMemberId(authenticatedMemberDto.id(), pageable);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish(
            @PathVariable Long wishId,
            @LoginMember AuthenticatedMemberDto authenticatedMemberDto
            ) {
        wishService.deleteWish(authenticatedMemberDto.id(), wishId);
        return ResponseEntity.noContent().build();
    }
}
