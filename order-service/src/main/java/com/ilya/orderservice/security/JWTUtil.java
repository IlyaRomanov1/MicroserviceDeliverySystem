package com.ilya.orderservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ilya.orderservice.dtos.requsets.AuthRequestDTO;
import com.ilya.orderservice.models.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {

    @Value("jwt_secret")
    private String secret;

    private AuthRequestDTO validateTokenAndRetrieveClaims(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("Person details")
                .withIssuer("auth-service")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        AuthRequestDTO dto = new AuthRequestDTO();
        dto.setId(jwt.getClaim("userId").asLong());
        dto.setRole(Role.valueOf(String.valueOf(jwt.getClaim("role"))));
        return dto;
    }
}
