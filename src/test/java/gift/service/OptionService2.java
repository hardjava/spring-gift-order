package gift.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OptionService2 {
    @Autowired
    private OptionService optionService;

    @Test
    void 동일한_상품_내의_이름이_중복() {
    }
}
