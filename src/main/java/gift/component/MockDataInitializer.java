package gift.component;

import gift.domain.Member;
import gift.domain.Option;
import gift.domain.Product;
import gift.domain.Wish;
import gift.enums.Role;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishListRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MockDataInitializer implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final WishListRepository wishListRepository;
    private final BCryptEncryptor bCryptEncryptor;

    public MockDataInitializer(MemberRepository memberRepository,
                               ProductRepository productRepository,
                               WishListRepository wishListRepository,
                               BCryptEncryptor bCryptEncryptor) {
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
        this.bCryptEncryptor = bCryptEncryptor;
        this.wishListRepository = wishListRepository;
    }

    @Override
    public void run(String... args) {
        Member m1 = memberRepository.save(new Member("test1@email.com", bCryptEncryptor.encode("1234"), Role.ROLE_USER));
        Member m2 = memberRepository.save(new Member("test2@email.com", bCryptEncryptor.encode("1234"), Role.ROLE_USER));
        Member m3 = memberRepository.save(new Member("test3@email.com", bCryptEncryptor.encode("1234"), Role.ROLE_USER));
        Member m4 = memberRepository.save(new Member("admin@email.com", bCryptEncryptor.encode("5678"), Role.ROLE_ADMIN));

        List<Product> products = List.of(
                new Product("핸드크림 세트", 12900L, "https://example.com/img1.jpg", List.of(
                        new Option("[Best] 시어버터 핸드 & 시어 스틱 립 밤", 3),
                        new Option("시어버터 핸드", 42)
                )),
                new Product("정원삼 365스틱 30포", 27800L, "https://example.com/img2.jpg", List.of(
                        new Option("[면역력 증진] 정원삼 6년근 고려홍삼정 365스틱 30포", 500)
                )),
                new Product("[단독] 샤워리 바디워시", 34500L, "https://example.com/img3.jpg", List.of(
                        new Option("시트러스", 10),
                        new Option("도넛피치", 520),
                        new Option("젖은나무", 230)
                )),
                new Product("수분 진정 크림", 15900L, "https://example.com/img4.jpg", List.of(
                        new Option("민감성용", 100),
                        new Option("지성용", 80)
                )),
                new Product("원두 커피 선물세트", 19900L, "https://example.com/img5.jpg", List.of(
                        new Option("콜롬비아 수프리모", 300),
                        new Option("에티오피아 예가체프", 150)
                )),
                new Product("프리미엄 티 세트", 9800L, "https://example.com/img6.jpg", List.of(
                        new Option("녹차", 200),
                        new Option("얼그레이", 120),
                        new Option("캐모마일", 90)
                )),
                new Product("건강한 견과 세트", 24800L, "https://example.com/img7.jpg", List.of(
                        new Option("믹스넛 1kg", 250),
                        new Option("아몬드", 120)
                )),
                new Product("디퓨저 2종 세트", 18700L, "https://example.com/img8.jpg", List.of(
                        new Option("로즈마리", 130),
                        new Option("라벤더", 160)
                )),
                new Product("여행용 스킨케어 키트", 21200L, "https://example.com/img9.jpg", List.of(
                        new Option("스킨 + 로션", 90),
                        new Option("클렌징폼", 60)
                )),
                new Product("건강즙 30포 세트", 32100L, "https://example.com/img10.jpg", List.of(
                        new Option("석류즙", 300),
                        new Option("배즙", 220)
                )),
                new Product("아이스크림", 32100L, "https://example.com/img11.jpg", List.of(
                        new Option("초코맛", 30),
                        new Option("딸기맛", 20),
                        new Option("바나나맛", 22)
                ))
        );

        products.forEach(productRepository::save);

        Wish wish = new Wish(m1, products.get(products.size() - 1));
        wishListRepository.save(wish);
    }
}
