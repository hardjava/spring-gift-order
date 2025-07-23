package com.example.demo.controller.product;

import com.example.demo.dto.product.ProductRequestDto;
import com.example.demo.dto.product.ProductResponseDto;
import com.example.demo.dto.product.ProductUpdateDto;
import com.example.demo.entity.Product;
import com.example.demo.service.product.ProductService;
import jakarta.validation.Valid;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/product-page")
public class ProductPageController {

  private final ProductService productService;

  public ProductPageController(ProductService productService) {
    this.productService = productService;
  }


  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("product", new ProductRequestDto());
    model.addAttribute("formAction", "/product-page");
    return "form";
  }

  @PostMapping
  public String createProduct(
      @ModelAttribute @Valid ProductRequestDto dto,
      BindingResult bindingResult,
      Model model
  ) {
      if(bindingResult.hasErrors()){
        model.addAttribute("product", dto);
        model.addAttribute("formAction", "/product-page");


        String errorMessage = bindingResult.getAllErrors().stream()
                                           .map(error -> error.getDefaultMessage())
                                           .collect(Collectors.joining("\n"));
        model.addAttribute("errorMessage", errorMessage);
        return "form";
      }
    productService.saveProduct(dto);
    return "redirect:/product-page";
  }

  @GetMapping("/{id}")
  public String showEditForm(@PathVariable Long id, Model model) {
    ProductResponseDto productToEdit = productService.productFindById(id);
    ProductUpdateDto updateDto = new ProductUpdateDto(
        productToEdit.id(),
        productToEdit.name(),
        productToEdit.price(),
        productToEdit.imageUrl()
    );

    model.addAttribute("product", updateDto);
    model.addAttribute("formAction", "/product-page/" + id + "/edit");

    return "form";
  }

  @PostMapping("/{id}/edit")
  public String updateProduct(@PathVariable Long id,
      @Valid @ModelAttribute("product") ProductUpdateDto dto,
      BindingResult bindingResult,
      Model model) {

    if (bindingResult.hasErrors()) {
      model.addAttribute("formAction", "/product-page/" + id + "/edit");
      return "form";
    }

    productService.productUpdateById(id, dto);
    return "redirect:/product-page";
  }

  @PostMapping("/{id}/delete")
  public String deleteProduct(@PathVariable Long id) {
    productService.productDeleteById(id);
    return "redirect:/product-page";
  }

  @GetMapping
  public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page){
    Page<Product> paging = this.productService.getList(page);
    model.addAttribute("paging", paging);
    return "list";
  }
}
