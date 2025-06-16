package com.anntraders.productservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users", schema = "anntraders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(
            name = "users_seq",
            sequenceName = "anntraders.users_id_seq",
            allocationSize = 1
    )
    private Long id;

    private String cognitosub;

    private String username;
    private String email;
    private String role;

    private String createdby;
    private java.time.LocalDateTime createtime;
    private String updatedby;
    private java.time.LocalDateTime updatetime;
}
