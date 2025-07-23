package com.example.demo.controller.product;

import com.example.demo.dto.product.ProductOptionRequestDto;
import com.example.demo.dto.product.ProductOptionResponseDto;
import com.example.demo.dto.product.ProductRequestDto;
import com.example.demo.dto.product.ProductResponseDto;
import com.example.demo.dto.product.ProductUpdateDto;
import com.example.demo.entity.ProductOption;
import com.example.demo.service.product.OptionService;
import com.example.demo.service.product.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.JdbcTransactionObjectSupport;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
  private final ProductService productService;
  private final OptionService optionService;

  public ProductController(ProductService productService, OptionService optionService){
    this.productService = productService;
    this.optionService = optionService;
  }

  @PostMapping
  public ResponseEntity<ProductResponseDto> addProduct(
      @Valid @RequestBody ProductRequestDto dto){
    return new ResponseEntity<>(productService.saveProduct(dto), HttpStatus.CREATED);
  }

  @PostMapping("/{productId}/options")
  public ResponseEntity<ProductOptionResponseDto> addOption(
      @PathVariable Long productId,
      @RequestBody @Valid ProductOptionRequestDto dto
  ){
    return new ResponseEntity<>(optionService.saveOption(productId, dto), HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductResponseDto> productFindById(@PathVariable Long id) {
    return new ResponseEntity<>(productService.productFindById(id),HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<ProductResponseDto>> productFindAll(){
    return new ResponseEntity<>(productService.productFindAll(), HttpStatus.OK);
  }

  @GetMapping("/{productId}/options")
  public ResponseEntity<List<ProductOptionResponseDto>> getOptions(@PathVariable Long productId){
    return new ResponseEntity<>(optionService.getOptions(productId), HttpStatus.OK);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ProductResponseDto> productUpdateById(
      @PathVariable Long id,
      @Valid @RequestBody ProductUpdateDto dto
  ){
    return new ResponseEntity<>(productService.productUpdateById(id, dto), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> productDeleteById(@PathVariable Long id){
    productService.productDeleteById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
