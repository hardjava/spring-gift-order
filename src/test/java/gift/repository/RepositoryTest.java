package gift.repository;

import gift.domain.*;
import gift.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class RepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    WishListRepository wishListRepository;

    @Test
    void 회원을_저장하면_id가_생성된다() {
        var member = new Member("test@email.com", "1234", Role.ROLE_USER);
        assertThat(member.getId()).isNull();
        var savedMember = memberRepository.save(member);
        assertThat(savedMember.getId()).isNotNull();
        assertThat(savedMember.getEmail()).isNotNull();
    }

    @Test
    void 이메일로_회원조회_시_존재하면_반환한다() {
        var member1 = memberRepository.findMemberByEmail("test@email.com");
        assertThat(member1).isEmpty();
        memberRepository.save(new Member("test@email.com", "1234", Role.ROLE_USER));
        var member2 = memberRepository.findMemberByEmail("test@email.com");
        assertThat(member2).isNotEmpty();
    }

    @Test
    void 이메일로_회원_존재여부를_확인할_수_있다() {
        assertThat(memberRepository.existsMemberByEmail("test@email.com")).isFalse();
        memberRepository.save(new Member("test@email.com", "1234", Role.ROLE_USER));
        assertThat(memberRepository.existsMemberByEmail("test@email.com")).isTrue();
    }

    @Test
    void 상품을_저장하면_id가_생성된다() {
        var product = new Product("[테스트] 쌍쌍바", 123400L, "테스트url");
        assertThat(product.getId()).isNull();
        var savedProduct = productRepository.save(product);
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isNotNull();
    }

    @Test
    void 상품정보를_수정하면_정상적으로_반영된다() {
        ProductName productName = new ProductName("[테스트] update");
        var product = productRepository.save(new Product("[테스트] 쌍쌍바", 123400L, "테스트url"));
        product.update("[테스트] update", 1200L, "수정 URL");
        var product2 = productRepository.findByName(productName);
        assertThat(product2).isNotEmpty();
    }

    @Test
    void wish가_존재하면_true를_반환한다() {
        // given
        Member member = memberRepository.save(new Member("test@example.com", "password", Role.ROLE_USER));
        Product product = productRepository.save(new Product("상품1", 1000L, "img.jpg"));
        wishListRepository.save(new Wish(member, product));
        // when
        boolean exists = wishListRepository.existsWishByMember_IdAndProduct_Id(member.getId(), product.getId());
        // then
        assertThat(exists).isTrue();
    }

    @Test
    void 회원과_상품id로_위시를_삭제하면_데이터가_없어진다() {
        // given
        Member member = memberRepository.save(new Member("test@example.com", "password", Role.ROLE_USER));
        Product product = productRepository.save(new Product("상품1", 1000L, "img.jpg"));
        Wish wish = wishListRepository.save(new Wish(member, product));
        // when
        wishListRepository.deleteWishByMember_IdAndProduct_Id(member.getId(), product.getId());
        // then
        assertThat(wishListRepository.findById(wish.getId())).isEmpty();
    }

    @Test
    void 검색어_없이_상품_페이징_정상조회() {
        // given
        Pageable pageRequest = PageRequest.of(0, 10, Sort.by("name").descending());
        PaginationInfo paginationInfo = new PaginationInfo(
                pageRequest,
                null
        );
        List<Product> mockProducts = List.of(
                new Product("상품1", 1000L, "img1"),
                new Product("상품2", 2000L, "img2")
        );
        productRepository.saveAll(mockProducts);

        // when
        Page<Product> result = productRepository.findProductByNameLike(paginationInfo.getPageable(), paginationInfo.getSearch());

        // then
        assertThat(result.getContent().size()).isEqualTo(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

    @Test
    void 검색어_있을때_상품_페이징_정상조회() {
        // given
        Pageable pageRequest = PageRequest.of(0, 10, Sort.by("name").descending());
        PaginationInfo paginationInfo = new PaginationInfo(
                pageRequest,
                "상품"
        );

        List<Product> mockProducts = List.of(
                new Product("상품검색1", 3000L, "img3"),
                new Product("상품검색2", 4000L, "img4"),
                new Product("테스트1", 4000L, "img4"),
                new Product("테스트2", 4000L, "img4")
        );
        productRepository.saveAll(mockProducts);

        // when
        Page<Product> result = productRepository.findProductByNameLike(paginationInfo.getPageable(), paginationInfo.getSearch());

        // then
        assertThat(result.getContent().size()).isEqualTo(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

    @Test
    void 검색어_없이_회원의_위시요약_페이징_정상조회() {
        // given
        var member = new Member("test@email.com", "1234", Role.ROLE_USER);
        var m = memberRepository.save(member);

        Pageable pageRequest = PageRequest.of(0, 10);
        WishListPaginationInfo wishListPaginationInfo = new WishListPaginationInfo(
                pageRequest,
                null,
                m
        );

        var p1 = productRepository.save(new Product("상품검색1", 3000L, "img3"));
        var p2 = productRepository.save(new Product("상품검색2", 4000L, "img4"));

        List<Wish> mockWished = List.of(
                new Wish(m, p1),
                new Wish(m, p1),
                new Wish(m, p1),
                new Wish(m, p2)
        );
        wishListRepository.saveAll(mockWished);

        // when
        Page<WishSummary> result = wishListRepository.findWishSummaryByMemberId(
                m.getId(),
                wishListPaginationInfo.getSearch(),
                wishListPaginationInfo.getPageable()
        );

        // then
        assertThat(result.getContent().size()).isEqualTo(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

    @Test
    void 검색어_있을때_회원의_위시요약_페이징_정상조회() {
        // given
        var member = new Member("test@email.com", "1234", Role.ROLE_USER);
        var m = memberRepository.save(member);

        Pageable pageRequest = PageRequest.of(0, 10);
        WishListPaginationInfo wishListPaginationInfo = new WishListPaginationInfo(
                pageRequest,
                "상품",
                m
        );

        var p1 = productRepository.save(new Product("상품검색1", 3000L, "img3"));
        var p2 = productRepository.save(new Product("테스트", 4000L, "img4"));

        List<Wish> mockWished = List.of(
                new Wish(m, p1),
                new Wish(m, p1),
                new Wish(m, p1),
                new Wish(m, p2)
        );
        wishListRepository.saveAll(mockWished);

        // when
        Page<WishSummary> result = wishListRepository.findWishSummaryByMemberId(
                m.getId(),
                wishListPaginationInfo.getSearch(),
                wishListPaginationInfo.getPageable()
        );

        // then
        assertThat(result.getContent().size()).isEqualTo(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

}
