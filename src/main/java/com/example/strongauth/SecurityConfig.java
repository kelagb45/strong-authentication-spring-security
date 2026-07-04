package com.example.strongauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomLoginSuccessHandler customLoginSuccessHandler;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          CustomLoginSuccessHandler customLoginSuccessHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customLoginSuccessHandler = customLoginSuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                .authorizeHttpRequests(auth -> auth

                        // Public authentication endpoints
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/refresh").permitAll()

                        // MFA
                        .requestMatchers(HttpMethod.GET, "/mfa/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/mfa/**").permitAll()

                        // Google Authenticator
                        .requestMatchers(HttpMethod.GET, "/totp/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/totp/**").permitAll()

                        // Public pages
                        .requestMatchers("/", "/register", "/form-login", "/public").permitAll()

                        // Test Audit endpoint (temporary)
                        .requestMatchers("/audit/**").permitAll()

                        // Role-based endpoints
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasRole("USER")

                        // Keycloak scope endpoints
                        .requestMatchers("/scope/email").hasAuthority("SCOPE_email")
                        .requestMatchers("/scope/profile").hasAuthority("SCOPE_profile")

                        // Protected endpoints
                        .requestMatchers("/api/secure").authenticated()
                        .requestMatchers("/oauth2/secure").authenticated()
                        .requestMatchers("/keycloak/**").authenticated()
                        .requestMatchers("/private").authenticated()

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/")
                        .loginProcessingUrl("/form-login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(customLoginSuccessHandler)
                        .failureUrl("/?error=true")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                )

                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> {})
                )

                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}