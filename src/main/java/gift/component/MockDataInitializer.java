package gift.component;

import gift.domain.Member;
import gift.domain.Option;
import gift.domain.Product;
import gift.enums.Role;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MockDataInitializer implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final BCryptEncryptor bCryptEncryptor;

    public MockDataInitializer(MemberRepository memberRepository,
                               ProductRepository productRepository,
                               BCryptEncryptor bCryptEncryptor) {
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
        this.bCryptEncryptor = bCryptEncryptor;
    }

    @Override
    public void run(String... args) {
        Member m1 = memberRepository.save(new Member("test1@email.com", bCryptEncryptor.encode("1234"), Role.ROLE_USER));
        Member m2 = memberRepository.save(new Member("test2@email.com", bCryptEncryptor.encode("1234"), Role.ROLE_USER));
        Member m3 = memberRepository.save(new Member("test3@email.com", bCryptEncryptor.encode("1234"), Role.ROLE_USER));
        Member m4 = memberRepository.save(new Member("admin@email.com", bCryptEncryptor.encode("5678"), Role.ROLE_ADMIN));

        List<Option> options = List.of(
                new Option("[Best] 시어버터 핸드 & 시어 스틱 립 밤", 3),
                new Option("시어버터 핸드", 42)
        );

        Product p1 = new Product("핸드크림 세트", 12900L, "https://example.com/img.jpg", options);
        productRepository.save(p1);
    }
}
