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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductDto dto, @RequestParam String code) {
        try {
            Product updated = productService.updateProduct(id, dto, code);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, @RequestParam String code) {
        try {
            productService.deleteProduct(id, code);
            return ResponseEntity.ok(Map.of("message", "Deleted"));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }
}
