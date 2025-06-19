package com.anntraders.productservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserInfo(String sub, String email, String username) {
}
