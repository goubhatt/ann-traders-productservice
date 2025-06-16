package com.anntraders.productservice.repository;

import com.anntraders.productservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCognitosub(String cognitosub);
}
