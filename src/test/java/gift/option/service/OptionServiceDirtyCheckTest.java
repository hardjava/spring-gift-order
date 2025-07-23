package gift.option.service;

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
class OptionServiceDirtyCheckTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Test
    @DisplayName("Option 엔티티 수정만으로 더티체킹이 발생해야 한다. ")
    void dirtyChecking_happensOnTransactionCommit() {
        Product product = productRepository.save(new Product("하리보 젤리", 1500, "http://img.url/test.png"));
        Option option = optionRepository.save(new Option(product, "낱개", 10));

        Option managed = entityManager.find(Option.class, option.getId());
        managed.update("묶음", 5);

        entityManager.flush();
        entityManager.clear();

        Option fromDb = optionRepository.findById(option.getId()).get();
        assertThat(fromDb.getName()).isEqualTo("묶음");
        assertThat(fromDb.getQuantity()).isEqualTo(5);
    }
}
