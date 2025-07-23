package gift.controller;

import gift.dto.OptionRequestDto;
import gift.dto.OptionResponseDto;
import gift.service.OptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/options")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping
    public ResponseEntity<List<OptionResponseDto>> list(
            @PathVariable Long productId
    ) {
        List<OptionResponseDto> options = optionService.getOptionsByProductId(productId);
        return ResponseEntity.ok(options);
    }

    @GetMapping("/{optionId}")
    public ResponseEntity<OptionResponseDto> get(
            @PathVariable Long productId,
            @PathVariable Long optionId
    ) {
        OptionResponseDto dto = optionService.getOptionByIdAndProductId(optionId, productId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<Void> create(
            @PathVariable Long productId,
            @RequestBody @Valid OptionRequestDto request
    ) {
        optionService.addOption(productId, request.name(), request.quantity());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{optionId}")
    public ResponseEntity<Void> update(
            @PathVariable Long productId,
            @PathVariable Long optionId,
            @RequestBody @Valid OptionRequestDto request
    ) {
        optionService.updateOption(productId, optionId, request.name(), request.quantity());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{optionId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long productId,
            @PathVariable Long optionId
    ) {
        optionService.deleteOption(productId, optionId);
        return ResponseEntity.noContent().build();
    }
}
