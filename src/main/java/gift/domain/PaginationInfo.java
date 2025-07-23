package gift.domain;

import org.springframework.data.domain.Pageable;

public class PaginationInfo {
    private Pageable pageable;
    private String search;

    public PaginationInfo(Pageable pageable, String search) {
        this.pageable = pageable;
        this.search = search;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public String getSearch() {
        return search;
    }
}
