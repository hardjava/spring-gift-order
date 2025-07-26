package gift.controller;

import gift.auth.LoginMember;
import gift.component.JwtUtil;
import gift.domain.Member;
import gift.domain.PaginationInfo;
import gift.dto.*;
import gift.service.OptionService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/products")
public class ProductController {
    private final ProductService productService;
    private final OptionService optionService;
    private final JwtUtil jwtUtil;

    public ProductController(ProductService productService, OptionService optionService, JwtUtil jwtUtil) {
        this.productService = productService;
        this.optionService = optionService;
        this.jwtUtil = jwtUtil;
    }

    // 상품 목록 조회
    @GetMapping
    public ResponseEntity<ProductWithPageResponseDto> findAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "name") String sortBy
    ) {
        Pageable pageRequest = PageRequest.of(page - 1, limit, Sort.by(sortBy).descending());
        PaginationInfo paginationInfo = new PaginationInfo(pageRequest, search);
        ProductWithPageResponseDto responseDto = productService.findAllProducts(paginationInfo);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 상품 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> findProductById(@PathVariable Long id) {
        ProductResponseDto productResponseDto = productService.findProductById(id);

        return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
    }

    // 상품 추가
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@LoginMember Member member, @Valid @RequestBody CreateProductRequestDto requestDto) {
        ProductResponseDto productResponseDto = productService.createProduct(requestDto);

        return new ResponseEntity<>(productResponseDto, HttpStatus.CREATED);
    }

    // 상품 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long id) {
        jwtUtil.validateAuthorizationHeader(authHeader, "products-api");
        productService.deleteProduct(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 상품 전체 수정
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequestDto requestDto
    ) {
        jwtUtil.validateAuthorizationHeader(authHeader, "products-api");
        productService.updateProduct(requestDto);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{productId}/options")
    public ResponseEntity<OptionResponseDto> findAllOptions(@PathVariable Long productId) {
        OptionResponseDto responseDto = optionService.getAllOptions(productId);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
