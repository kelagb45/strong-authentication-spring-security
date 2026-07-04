package com.example.strongauth;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final AppUserRepository appUserRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               AppUserRepository appUserRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.appUserRepository = appUserRepository;
    }

    public RefreshToken createRefreshToken(String username) {

        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        refreshTokenRepository.findByUser(user)
                .ifPresent(existingToken -> {
                    refreshTokenRepository.delete(existingToken);
                    refreshTokenRepository.flush();
                });

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusSeconds(7 * 24 * 60 * 60));

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyRefreshToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired");
        }

        return refreshToken;
    }
}