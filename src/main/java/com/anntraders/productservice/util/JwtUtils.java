package com.anntraders.productservice.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;

public class JwtUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static JsonNode decodeJWT(String jwtToken) throws Exception {
        String[] parts = jwtToken.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid JWT token");
        }

        byte[] decodedBytes = Base64.getUrlDecoder().decode(parts[1]);
        String payloadJson = new String(decodedBytes);
        return objectMapper.readTree(payloadJson);
    }
}
