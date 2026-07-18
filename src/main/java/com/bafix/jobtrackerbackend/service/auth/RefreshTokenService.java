package com.bafix.jobtrackerbackend.service.auth;

import com.bafix.jobtrackerbackend.entity.User;
import com.bafix.jobtrackerbackend.refresh.RefreshToken;
import com.bafix.jobtrackerbackend.refresh.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.jwt.refreshExpirationSeconds:1209600}")
    private long refreshExpirationSeconds;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(User user){
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Instant.now().plusSeconds(refreshExpirationSeconds));
        return refreshTokenRepository.save(token);
    }

    public boolean isValid(RefreshToken token){
        return token != null && token.getExpiryDate().isAfter(Instant.now());
    }

    public RefreshToken findByToken(String token){
        return refreshTokenRepository.findByToken(token).orElse(null);
    }

    public void deleteByUserId(Long userId){
        refreshTokenRepository.deleteByUserId(userId);
    }
}
