package gift.member.service;

import gift.option.entity.Option;
import gift.option.repository.OptionRepository;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(properties = {
        "spring.sql.init.mode=never",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
class MemberServiceDirtyCheckTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("더티체킹으로 Member 이름을 수정하면 트랜잭션 커밋 시 DB에 반영된다. ")
    void dirtyCheck_updatesMemberName() {
        Product product = productRepository.save(new Product("하리보 젤리", 1500, "http://img.url/test.png"));

        Option opt = optionRepository.save(new Option(product, "낱개", 10));
        entityManager.flush();
        entityManager.clear();

        Option managed = entityManager.find(Option.class, opt.getId());
        managed.update("묶음", 5);

        entityManager.flush();
        entityManager.clear();

        Option fromDb = optionRepository.findById(opt.getId()).get();
        assertThat(fromDb.getName()).isEqualTo("묶음");
        assertThat(fromDb.getQuantity()).isEqualTo(5);
    }
}
