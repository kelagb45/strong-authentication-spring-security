package com.example.strongauth;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/totp")
public class TotpController {

    private final AppUserRepository repository;
    private final TotpService totpService;

    public TotpController(AppUserRepository repository,
                          TotpService totpService) {
        this.repository = repository;
        this.totpService = totpService;
    }

    @GetMapping("/setup")
    public String setup(@RequestParam String username) {

        AppUser user = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String secret = totpService.generateSecret();
        user.setTotpSecret(secret);
        repository.save(user);

        String qrUrl = "otpauth://totp/StrongAuth:" + username
                + "?secret=" + secret
                + "&issuer=StrongAuth";

        return """
                <html>
                <body style="font-family:Segoe UI;text-align:center;margin-top:80px;">
                    <h2>Google Authenticator Setup</h2>
                    <p>Copy this secret into Google Authenticator:</p>
                    <h3>%s</h3>
                    <p>Or use this setup link:</p>
                    <p>%s</p>
                    <a href="/totp/verify-page?username=%s">Verify Code</a>
                </body>
                </html>
                """.formatted(secret, qrUrl, username);
    }

    @GetMapping("/verify-page")
    public String verifyPage(@RequestParam String username) {
        return """
                <html>
                <body style="font-family:Segoe UI;text-align:center;margin-top:80px;">
                    <h2>Verify Google Authenticator Code</h2>
                    <form action="/totp/verify" method="post">
                        <input type="hidden" name="username" value="%s">
                        <input type="text" name="code" placeholder="Enter 6-digit code">
                        <button type="submit">Verify</button>
                    </form>
                </body>
                </html>
                """.formatted(username);
    }

    @PostMapping("/verify")
    public String verify(@RequestParam String username,
                         @RequestParam String code) {

        AppUser user = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean valid = totpService.verifyCode(user.getTotpSecret(), code);

        if (valid) {
            return "TOTP verification successful. Access granted.";
        }

        return "Invalid TOTP code. Access denied.";
    }
}