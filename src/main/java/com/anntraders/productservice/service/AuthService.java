package com.anntraders.productservice.service;

import com.anntraders.productservice.model.User;
import com.anntraders.productservice.repository.UserRepository;
import com.anntraders.productservice.util.JwtUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final WebClient webClient = WebClient.create();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${auth.local-bypass-enabled:false}")
    private boolean localBypassEnabled;

    @Value("${cognito.token-uri}")
    private String tokenUri;

    @Value("${cognito.client-id}")
    private String clientId;

    @Value("${cognito.client-secret}")
    private String clientSecret;

    @Value("${cognito.redirect-uri}")
    private String redirectUri;

    public String exchangeCodeForToken(String code) {
        try {
            log.debug("Sending request to: {}", tokenUri);
            log.debug("With client_id={}, redirect_uri={}, code={}", clientId, redirectUri, code);

            String responseBody = webClient.post()
                    .uri(tokenUri)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .bodyValue(
                            "grant_type=authorization_code" +
                                    "&client_id=" + clientId +
                                    "&client_secret=" + clientSecret +
                                    "&redirect_uri=" + redirectUri +
                                    "&code=" + code
                    )
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnNext(body -> log.debug("Cognito token response: {}", body))
                    .block();

            JsonNode tokenResponse = objectMapper.readTree(responseBody);
            return tokenResponse.has("id_token") ? tokenResponse.get("id_token").asText() : null;

        } catch (WebClientResponseException ex) {
            log.error("Token exchange failed: {}", ex.getResponseBodyAsString(), ex);
            throw new RuntimeException("Invalid or expired authorization code");
        } catch (Exception ex) {
            log.error("Unexpected token exchange failure", ex);
            throw new RuntimeException("Token exchange failed");
        }
    }

    public User validateOrCreateUser(String code) {
        String cognitosub;
        String email;
        String username;

        if (localBypassEnabled) {
            log.warn("Local bypass enabled. Using dummy user values.");
            cognitosub = "gbsub";
            email = "mailto:goubhatt@in.ibm.com";
            username = "goutam";
        } else {
            String idToken = exchangeCodeForToken(code);
            if (idToken == null) throw new RuntimeException("ID token missing");

            try {
                JsonNode payload = JwtUtils.decodeJWT(idToken);
                cognitosub = payload.get("sub").asText();
                email = payload.get("email").asText();
                username = payload.has("cognito:username") ? payload.get("cognito:username").asText() : email;
            } catch (Exception ex) {
                log.error("Failed to decode token payload", ex);
                throw new RuntimeException("Token decoding failed");
            }
        }

        return userRepository.findByCognitosub(cognitosub)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setCognitosub(cognitosub);
                    newUser.setEmail(email);
                    newUser.setUsername(username);
                    newUser.setRole("user");
                    newUser.setCreatedby("anntraderadmin");
                    newUser.setCreatetime(LocalDateTime.now());

                    try {
                        return userRepository.save(newUser);
                    } catch (DataIntegrityViolationException e) {
                        log.warn("User already inserted by another thread for sub={}", cognitosub);
                        return userRepository.findByCognitosub(cognitosub)
                                .orElseThrow(() -> new RuntimeException("User creation failed and user not found"));
                    }
                });


    }

}
