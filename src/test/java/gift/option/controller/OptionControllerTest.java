package gift.option.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.option.dto.OptionCreateRequestDto;
import gift.option.dto.OptionUpdateRequestDto;
import gift.option.entity.Option;
import gift.option.repository.OptionRepository;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Product product;

    @BeforeEach
    void setUp() {
        optionRepository.deleteAll();
        productRepository.deleteAll();

        product = productRepository.save(
                new Product("하리보 젤리", 2000, "http://img.url/test.png")
        );
    }

    @Test
    @DisplayName("옵션 생성 시, 201(Created)를 반환한다. ")
    void createOption_returns201() throws Exception {
        OptionCreateRequestDto createDto = new OptionCreateRequestDto("낱개", 10);

        mockMvc.perform(post("/api/products/{id}/options", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("옵션 조회 시, 200(OK) 및 리스트 반환한다. ")
    void getOptions_returnsOkAndList() throws Exception {
        optionRepository.saveAll(List.of(
                new Option(product, "낱개", 10),
                new Option(product, "묶음", 5)
        ));

        mockMvc.perform(get("/api/products/{id}/options", product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[?(@.name=='낱개')].quantity", contains(10)))
                .andExpect(jsonPath("$[?(@.name=='묶음')].quantity", contains(5)));
    }

    @Test
    @DisplayName("옵션 수정 시, 200(OK)를 반환한다. ")
    void updateOption_returns200() throws Exception {
        Option opt = optionRepository.save(new Option(product, "낱개", 10));
        OptionUpdateRequestDto updateDto = new OptionUpdateRequestDto("묶음", 20);

        mockMvc.perform(patch("/api/products/{pid}/options/{oid}", product.getId(), opt.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(opt.getId()))
                .andExpect(jsonPath("$.name").value("묶음"))
                .andExpect(jsonPath("$.quantity").value(20));
    }

    @Test
    @DisplayName("중복 이름일 경우, 400(Bad Request)를 반환한다. ")
    void updateOption_duplicateName_returns400() throws Exception {
        optionRepository.save(new Option(product, "낱개", 10));
        Option option = optionRepository.save(new Option(product, "묶음", 5));

        OptionUpdateRequestDto updateDto = new OptionUpdateRequestDto("낱개", 5);

        mockMvc.perform(patch("/api/products/{pid}/options/{oid}", product.getId(), option.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("같은 상품 내에 동일한 옵션명이 존재합니다.")));
    }

    @Test
    @DisplayName("옵션 생성 시, 특수문자 에러 400 반환한다. ")
    void createOption_withBadCharacters_returns400() throws Exception {
        var badDto = new OptionCreateRequestDto("잘못된 상품명!!", 5);

        mockMvc.perform(post("/api/products/{id}/options", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("허용되지 않는 특수문자가 포함되어 있습니다.")));
    }
}
