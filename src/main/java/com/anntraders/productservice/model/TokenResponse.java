package com.anntraders.productservice.model;

public record TokenResponse(
        String id_token,
        String access_token,
        String refresh_token,
        String token_type,
        int expires_in
) {}
