package gift.wish.controller;

import gift.member.dto.AuthenticatedMemberDto;
import gift.security.annotation.LoginMember;
import gift.wish.dto.WishResponseDto;
import gift.wish.service.WishService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/wishes")
public class WishViewController {

    private final WishService wishService;

    public WishViewController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public String wishList(@LoginMember AuthenticatedMemberDto member,
                           @PageableDefault(size = 5) Pageable pageable,
                           Model model) {
        Page<WishResponseDto> page = wishService.findAllWishesByMemberId(member.id(), pageable);
        model.addAttribute("page", page);
        return "wish/list";
    }
}
