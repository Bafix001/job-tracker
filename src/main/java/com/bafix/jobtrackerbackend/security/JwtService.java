package com.bafix.jobtrackerbackend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.bafix.jobtrackerbackend.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.accessExpirationSeconds:900}")
    private long accessExpirationSeconds;

    @Value("${app.jwt.refreshExpirationSeconds:1209600}")
    private long refreshExpirationSeconds;

    public String generateAccessToken(User user){
        Algorithm alg = Algorithm.HMAC256(jwtSecret);
        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("id", user.getId())
                .withIssuedAt(Date.from(Instant.now()))
                .withExpiresAt(Date.from(Instant.now().plusSeconds(accessExpirationSeconds)))
                .sign(alg);
    }

    public String generateRefreshToken(User user){
        Algorithm alg = Algorithm.HMAC256(jwtSecret);
        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("id", user.getId())
                .withIssuedAt(Date.from(Instant.now()))
                .withExpiresAt(Date.from(Instant.now().plusSeconds(refreshExpirationSeconds)))
                .sign(alg);
    }

    public DecodedJWT verifyToken(String token){
        Algorithm alg = Algorithm.HMAC256(jwtSecret);
        JWTVerifier verifier = JWT.require(alg).build();
        return verifier.verify(token);
    }

}
