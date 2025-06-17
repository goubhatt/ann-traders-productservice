package com.anntraders.productservice.service;

import com.anntraders.productservice.dto.ProductDto;
import com.anntraders.productservice.model.Product;
import com.anntraders.productservice.model.User;
import com.anntraders.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final AuthService authService;

    public Product addProduct(ProductDto dto, String code) {
        User user = authService.validateOrCreateUser(code);
        Product product = new Product(
                null,
                dto.name(),
                dto.description(),
                dto.price(),
                user.getId(),  // ownerId
                user.getUsername(),
                LocalDateTime.now(),
                null,
                null
        );
        return productRepository.save(product);
    }

    public List<Product> getAllProducts(String code) {
        User user = authService.validateOrCreateUser(code);
        return productRepository.findByOwnerid(user.getId());
    }

    public Product updateProduct(Long id, ProductDto dto, String code) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        existing.setName(dto.name());
        existing.setDescription(dto.description());
        existing.setPrice(dto.price());
        existing.setUpdatedby(dto.ownerId());
        existing.setUpdatetime(LocalDateTime.now());

        return productRepository.save(existing);
    }

    public void deleteProduct(Long id, String code) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        // optional: audit trail using `code`

        productRepository.delete(product);
    }
}
