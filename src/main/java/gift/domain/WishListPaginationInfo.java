package gift.domain;

import org.springframework.data.domain.Pageable;

public class WishListPaginationInfo extends PaginationInfo {
    private Member member;

    public WishListPaginationInfo(Pageable pageable, String search, Member member) {
        super(pageable, search);
        this.member = member;
    }

    public Long getMemberId() {
        return member.getId();
    }
}
