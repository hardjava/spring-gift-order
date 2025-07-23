package gift.option.controller;

import gift.option.dto.OptionCreateRequestDto;
import gift.option.service.OptionService;
import gift.product.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/products/{productId}/options")
public class AdminProductOptionViewController {

    private final ProductService productService;
    private final OptionService optionService;

    public AdminProductOptionViewController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    @GetMapping
    public String productDetail(
            @PathVariable("productId") Long productId,
            Model model
    ) {
        model.addAttribute("product", productService.findProductById(productId));
        model.addAttribute("options", optionService.getOptions(productId));
        return "product/product-detail";
    }

    @PostMapping
    public String addOption(
            @PathVariable("productId") Long productId,
            @RequestParam String name,
            @RequestParam int quantity
    ) {
        optionService.createOption(productId, new OptionCreateRequestDto(name, quantity));
        return "redirect:/admin/products/" + productId;
    }
}
