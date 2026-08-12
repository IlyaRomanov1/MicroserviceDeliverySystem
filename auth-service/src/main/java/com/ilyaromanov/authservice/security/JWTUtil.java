package com.ilyaromanov.authservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ilyaromanov.authservice.models.dtos.responses.AuthResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {

    @Value("jwt_secret")
    private String secret;

    public String generateToken(AuthResponseDTO dto) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusHours(2L).toInstant());

        return JWT.create()
                .withSubject("Person details")
                .withClaim("userId", dto.getId())
                .withClaim("role", dto.getRole().toString())
                .withIssuedAt(new Date())
                .withIssuer("auth-service")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    //TODO Verifier и extract claims в order-service
}
