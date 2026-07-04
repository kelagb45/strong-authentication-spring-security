package com.example.strongauth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final AuditLogService auditLogService;

    public AuthApiController(AuthenticationManager authenticationManager,
                             JwtUtil jwtUtil,
                             RefreshTokenService refreshTokenService,
                             AuditLogService auditLogService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.auditLogService = auditLogService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        String accessToken = jwtUtil.generateToken(request.getUsername());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(request.getUsername());

        auditLogService.log(request.getUsername(), "LOGIN_SUCCESS");

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    @PostMapping("/refresh")
    public AuthResponse refreshToken(@RequestBody RefreshTokenRequest request) {

        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(
                request.getRefreshToken()
        );

        String username = refreshToken.getUser().getUsername();
        String newAccessToken = jwtUtil.generateToken(username);

        auditLogService.log(username, "REFRESH_TOKEN_USED");

        return new AuthResponse(newAccessToken, refreshToken.getToken());
    }
}