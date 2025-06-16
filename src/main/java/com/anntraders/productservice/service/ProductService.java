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

    private final ProductRepository repository;
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
        return repository.save(product);
    }

    public List<Product> getAllProducts(String code) {
        User user = authService.validateOrCreateUser(code);
        return repository.findByOwnerid(user.getId());
    }
}
