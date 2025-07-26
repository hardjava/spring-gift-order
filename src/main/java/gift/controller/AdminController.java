package gift.controller;

import gift.component.JwtUtil;
import gift.domain.PaginationInfo;
import gift.dto.CreateProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.ProductWithPageResponseDto;
import gift.dto.UpdateProductRequestDto;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/products")
public class AdminController {
    private final ProductService productService;
    private final JwtUtil jwtUtil;

    public AdminController(ProductService productService, JwtUtil jwtUtil) {
        this.productService = productService;
        this.jwtUtil = jwtUtil;
    }

    // 상품 목록 조회
    @GetMapping
    public String list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "name") String sortBy,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            Model model) {
        jwtUtil.validateAuthorizationAdminHeader(authHeader, "admin-api");
        Pageable pageRequest = PageRequest.of(page - 1, limit, Sort.by(sortBy).descending());
        PaginationInfo paginationInfo = new PaginationInfo(pageRequest, search);
        ProductWithPageResponseDto responseDto = productService.findAllProducts(paginationInfo);
        model.addAttribute("products", responseDto);

        return "admin/product/list";
    }

    // 상품 단건 조회
    @GetMapping("/{id}")
    public String detail(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long id,
            Model model) {
        jwtUtil.validateAuthorizationAdminHeader(authHeader, "admin-api");
        ProductResponseDto findProduct = productService.findProductById(id);
        model.addAttribute("product", findProduct);

        return "admin/product/detail";
    }

    // 상품 등록 페이지
    @GetMapping("/new")
    public String createProduct(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            Model model) {
        jwtUtil.validateAuthorizationAdminHeader(authHeader, "admin-api");
        model.addAttribute("product", new CreateProductRequestDto("", 0L, "", null));

        return "admin/product/create";
    }

    // 상품 등록
    @PostMapping("/new")
    public String createProduct(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @Valid @ModelAttribute("product") CreateProductRequestDto requestDto,
            BindingResult bindingResult) {
        jwtUtil.validateAuthorizationAdminHeader(authHeader, "admin-api");
        if (bindingResult.hasErrors()) {

            return "admin/product/create";
        }
        productService.createProduct(requestDto);

        return "redirect:/admin/products";
    }

    // 상품 삭제
    @DeleteMapping("/{id}")
    public String deleteProduct(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long id) {
        jwtUtil.validateAuthorizationAdminHeader(authHeader, "admin-api");
        productService.deleteProduct(id);

        return "redirect:/admin/products";
    }

    // 상품 수정 페이지
    @GetMapping("/edit/{id}")
    public String updateProduct(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long id, Model model) {
        jwtUtil.validateAuthorizationAdminHeader(authHeader, "admin-api");
        ProductResponseDto findProduct = productService.findProductById(id);
        model.addAttribute("product", new UpdateProductRequestDto(
                findProduct.id(),
                findProduct.name(),
                findProduct.price(),
                findProduct.imageUrl())
        );

        return "admin/product/update";
    }

    // 상품 수정
    @PutMapping("/edit/{id}")
    public String updateProduct(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long id,
            @Valid @ModelAttribute("product") UpdateProductRequestDto updatedProduct,
            BindingResult bindingResult) {
        jwtUtil.validateAuthorizationAdminHeader(authHeader, "admin-api");
        if (bindingResult.hasErrors()) {

            return "admin/product/update";
        }
        productService.updateProduct(updatedProduct);

        return "redirect:/admin/products";
    }
}
