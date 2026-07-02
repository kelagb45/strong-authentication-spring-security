package com.example.strongauth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mfa")
public class MfaController {

    private final AuthenticationManager authenticationManager;
    private final OtpService otpService;

    public MfaController(AuthenticationManager authenticationManager,
                         OtpService otpService) {
        this.authenticationManager = authenticationManager;
        this.otpService = otpService;
    }

    @PostMapping("/login")
    public String loginWithPassword(@RequestParam String username,
                                    @RequestParam String password) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        otpService.generateOtp(username);

        return "Password correct. OTP has been generated. Check IntelliJ console.";
    }

    @PostMapping("/verify")
    public String verifyOtp(@RequestParam String username,
                            @RequestParam String otp) {

        boolean valid = otpService.verifyOtp(username, otp);

        if (valid) {
            return "MFA successful. Access granted.";
        }

        return "Invalid OTP. Access denied.";
    }
}