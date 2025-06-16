package com.anntraders.productservice.dto;

import java.math.BigDecimal;

public record ProductDto(
        String name,
        String description,
        BigDecimal price,
        String ownerId
) {}