package com.anntraders.productservice.controller;


import com.anntraders.productservice.dto.ProductDto;
import com.anntraders.productservice.model.Product;
import com.anntraders.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody ProductDto dto, @RequestParam String code) {
        try {
            Product product = productService.addProduct(dto, code);
            return ResponseEntity.ok(product);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam String code) {
        return ResponseEntity.ok(productService.getAllProducts(code));
    }

/*    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody ProductDto dto, @RequestParam String code) {
        return ResponseEntity.ok(productService.updateProduct(id, dto, code));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, @RequestParam String code) {
        productService.deleteProduct(id, code);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam String code,
            @RequestParam(required = false) String name
    ) {
        return ResponseEntity.ok(productService.searchProducts(code, name));
    }*/
}
