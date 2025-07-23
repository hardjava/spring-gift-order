package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.CreateOptionRequestDto;
import gift.dto.CreateProductRequestDto;
import gift.dto.OptionResponseDto;
import gift.dto.ProductPageDto;
import gift.dto.ProductResponseDto;
import gift.dto.UpdateOptionQuantityRequestDto;
import gift.dto.UpdateProductRequestDto;
import gift.entity.Member;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.service.OptionService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    private final OptionService optionService;

    public ProductController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(
            @Valid @RequestBody CreateProductRequestDto requestDto,
            @LoginMember Member member) {
        if (requestDto.name().contains("카카오") && !member.isAdmin()) {
            throw new CustomException(ErrorCode.NamingForbidden);
        }
        return new ResponseEntity<>(productService.createProduct(requestDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ProductPageDto> findAllProducts(
            @PageableDefault(size = 5, sort = "name", direction = Direction.ASC) Pageable pageable) {
        ProductPageDto products = productService.findAllProducts(pageable);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> findProductById(@PathVariable Long id) {
        return new ResponseEntity<>(productService.findProductById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/options")
    public ResponseEntity<List<OptionResponseDto>> findProductOptionById(@PathVariable Long id) {
        return new ResponseEntity<>(optionService.findProductOptionById(id), HttpStatus.OK);
    }

    @PostMapping("/{id}/options")
    public ResponseEntity<OptionResponseDto> createOption(
            @PathVariable Long id,
            @Valid @RequestBody CreateOptionRequestDto requestDto) {
        return new ResponseEntity<>(optionService.createOption(requestDto, id), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/options/{optionId}")
    public ResponseEntity<OptionResponseDto> setOptionQuantity(
            @PathVariable Long id,
            @PathVariable Long optionId,
            @Valid @RequestBody UpdateOptionQuantityRequestDto requestDto) {
        return new ResponseEntity<>(optionService.setOptionQuantity(id, optionId, requestDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/options/{optionId}")
    public ResponseEntity<Void> deleteOption(
            @PathVariable Long id,
            @PathVariable Long optionId) {
        optionService.deleteOption(id,optionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProductById(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequestDto requestDto,
            @LoginMember Member member) {
        if (requestDto.name().contains("카카오") && !member.isAdmin()) {
            throw new CustomException(ErrorCode.NamingForbidden);
        }

        return new ResponseEntity<>(productService.updateProductById(id, requestDto),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(
            @PathVariable Long id) {
        productService.deleteProductById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
