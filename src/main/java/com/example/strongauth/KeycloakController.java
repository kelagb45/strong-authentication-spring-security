package com.example.strongauth;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KeycloakController {

    @GetMapping("/keycloak/profile")
    public String keycloakProfile(Authentication authentication) {

        return """
                Keycloak OpenID Connect Authentication Successful.
                Authenticated Principal: %s
                Authorities: %s
                """.formatted(
                authentication.getName(),
                authentication.getAuthorities()
        );
    }
}