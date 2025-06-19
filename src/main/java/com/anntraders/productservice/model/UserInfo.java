package com.anntraders.productservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserInfo(
        String sub,
        String email,
        @JsonProperty("cognito:username") String username
) {}
