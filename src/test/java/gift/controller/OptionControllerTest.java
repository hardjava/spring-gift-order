package gift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.advice.GlobalExceptionHandler;
import gift.dto.OptionRequestDto;
import gift.dto.OptionResponseDto;
import gift.service.OptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OptionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OptionService optionService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
                .standaloneSetup(new OptionController(optionService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void listOptions_ReturnsList() throws Exception {
        OptionResponseDto dto = new OptionResponseDto(1L, "opt", 5);
        given(optionService.getOptionsByProductId(1L)).willReturn(List.of(dto));

        mockMvc.perform(get("/api/products/1/options"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("opt"))
                .andExpect(jsonPath("$[0].quantity").value(5));
    }

    @Test
    void getOption_ReturnsOption() throws Exception {
        OptionResponseDto dto = new OptionResponseDto(2L, "opt2", 10);
        given(optionService.getOptionByIdAndProductId(2L, 1L)).willReturn(dto);

        mockMvc.perform(get("/api/products/1/options/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("opt2"))
                .andExpect(jsonPath("$.quantity").value(10));
    }

    @Test
    void createOption_ReturnsCreated() throws Exception {
        OptionRequestDto req = new OptionRequestDto("opt", 5);
        mockMvc.perform(post("/api/products/1/options")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        then(optionService).should().addOption(1L, "opt", 5);
    }

    @Test
    void updateOption_ReturnsOk() throws Exception {
        OptionRequestDto req = new OptionRequestDto("upd", 10);
        mockMvc.perform(put("/api/products/1/options/2")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        then(optionService).should().updateOption(1L, 2L, "upd", 10);
    }

    @Test
    void deleteOption_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/products/1/options/2"))
                .andExpect(status().isNoContent());

        then(optionService).should().deleteOption(1L, 2L);
    }
}
