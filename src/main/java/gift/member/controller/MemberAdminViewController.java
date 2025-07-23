package gift.member.controller;

import gift.member.dto.MemberResponseDto;
import gift.member.dto.MemberUpdateRequestDto;
import gift.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/members")
public class MemberAdminViewController {

    private final MemberService memberService;
    public MemberAdminViewController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String list(
            @PageableDefault(size = 10) Pageable pageable,
            Model model
    ) {
        Page<MemberResponseDto> page = memberService.findAllMembers(pageable);
        model.addAttribute("page", page);
        return "member/list";
    }

    @GetMapping("/{id}/update")
    public String updateForm(@PathVariable Long id, Model model) {
        MemberResponseDto m = memberService.findMemberById(id);
        model.addAttribute("id", id);
        model.addAttribute("dto", new MemberUpdateRequestDto(m.name(), m.email(), ""));
        return "member/update";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, @ModelAttribute MemberUpdateRequestDto dto) {
        memberService.updateMember(id, dto);
        return "redirect:/admin/members";
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }
}
