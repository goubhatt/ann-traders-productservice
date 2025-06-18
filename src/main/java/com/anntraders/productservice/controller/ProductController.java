package com.anntraders.productservice.controller;

import com.anntraders.productservice.dto.ProductDto;
import com.anntraders.productservice.model.Product;
import com.anntraders.productservice.model.UserInfo;
import com.anntraders.productservice.service.CognitoService;
import com.anntraders.productservice.service.ProductService;
import com.anntraders.productservice.model.TokenResponse;
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
    private final CognitoService cognitoService;

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody ProductDto dto, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            UserInfo userInfo = cognitoService.extractUserInfo(token);
            Product product = productService.addProduct(dto, userInfo);
            return ResponseEntity.ok(product);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UserInfo userInfo = cognitoService.extractUserInfo(token);
        return ResponseEntity.ok(productService.getAllProducts(userInfo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductDto dto, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            UserInfo userInfo = cognitoService.extractUserInfo(token);
            Product updated = productService.updateProduct(id, dto, userInfo);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            UserInfo userInfo = cognitoService.extractUserInfo(token);
            productService.deleteProduct(id, userInfo);
            return ResponseEntity.ok(Map.of("message", "Deleted"));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/auth/validate-code")
    public ResponseEntity<?> validateCode(@RequestParam String code) {
        TokenResponse tokenResponse = cognitoService.exchangeCodeForToken(code);
        UserInfo userInfo = cognitoService.extractUserInfo(tokenResponse.id_token());
        return ResponseEntity.ok(Map.of(
                "idToken", tokenResponse.id_token(),
                "userInfo", userInfo
        ));
    }
}
