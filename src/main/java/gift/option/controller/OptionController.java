package gift.option.controller;

import gift.option.dto.OptionCreateRequestDto;
import gift.option.dto.OptionResponseDto;
import gift.option.dto.OptionUpdateRequestDto;
import gift.option.service.OptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/{productId}/options")
    public ResponseEntity<List<OptionResponseDto>> getOptions(@PathVariable Long productId) {
        return ResponseEntity.ok(optionService.getOptions(productId));
    }

    @PostMapping("/{productId}/options")
    public ResponseEntity<OptionResponseDto> createOption(
            @PathVariable Long productId,
            @Valid @RequestBody OptionCreateRequestDto request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(optionService.createOption(productId, request));

    }

    @PatchMapping("/{productId}/options/{optionId}")
    public ResponseEntity<OptionResponseDto> updateOption(
            @PathVariable Long productId,
            @PathVariable Long optionId,
            @Valid @RequestBody OptionUpdateRequestDto request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(optionService.updateOption(productId, optionId, request));
    }

}
