package com.anntraders.productservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products", schema = "anntraders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @SequenceGenerator(
            name = "product_seq",
            sequenceName = "anntraders.products_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;

    private Long ownerid;

    private String createdby;
    private LocalDateTime createtime;
    private String updatedby;
    private LocalDateTime updatetime;
}