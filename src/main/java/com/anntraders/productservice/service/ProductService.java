package com.anntraders.productservice.service;

import com.anntraders.productservice.dto.ProductDto;
import com.anntraders.productservice.model.Product;
import com.anntraders.productservice.model.User;
import com.anntraders.productservice.model.UserInfo;
import com.anntraders.productservice.repository.ProductRepository;
import com.anntraders.productservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public Product addProduct(ProductDto dto, UserInfo userInfo) {
        User user = getOrCreateUser(userInfo);
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

    public List<Product> getAllProducts(UserInfo userInfo) {
        User user = getOrCreateUser(userInfo);
        return productRepository.findByOwnerid(user.getId());
    }

    public Product updateProduct(Long id, ProductDto dto, UserInfo userInfo) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        existing.setName(dto.name());
        existing.setDescription(dto.description());
        existing.setPrice(dto.price());
        existing.setUpdatedby(userInfo.username());
        existing.setUpdatetime(LocalDateTime.now());

        return productRepository.save(existing);
    }

    public void deleteProduct(Long id, UserInfo userInfo) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        // optional: audit trail using userInfo.sub() or username()

        productRepository.delete(product);
    }

    private User getOrCreateUser(UserInfo userInfo) {
        return userRepository.findByCognitosub(userInfo.sub())
                .orElseGet(() -> userRepository.save(new User(
                        null,
                        userInfo.sub(),
                        userInfo.email(),
                        userInfo.email(),
                        "USER",
                        userInfo.username(),
                        LocalDateTime.now(),
                        null,
                        null
                )));
    }
}
