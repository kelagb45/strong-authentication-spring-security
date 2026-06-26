package com.example.strongauth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AppUserRepository repository,
                          PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String loginPage(@RequestParam(required = false) String error) {
        String message = "";

        if ("true".equals(error)) {
            message = "<p style='color:red;font-weight:bold;'>Wrong username or password</p>";
        }

        return buildLoginPage(message);
    }

    @GetMapping("/register")
    public String registerPage() {
        return """
                <html>
                <body style="margin:0;font-family:Segoe UI;background:linear-gradient(135deg,#43cea2,#185a9d);display:flex;justify-content:center;align-items:center;height:100vh;">
                    <div style="background:white;padding:40px;border-radius:20px;width:360px;box-shadow:0 10px 25px rgba(0,0,0,0.25);text-align:center;">
                        <h2>Create Account</h2>
                        <p style="color:#666;">Register securely with BCrypt and PostgreSQL</p>

                        <form action="/register" method="post" autocomplete="off">
                            <input type="text" name="username" placeholder="Username" autocomplete="off" value=""
                                   style="width:90%;padding:12px;margin:10px;border-radius:8px;border:1px solid #ccc;">

                            <input type="password" name="password" placeholder="Password" autocomplete="new-password" value=""
                                   style="width:90%;padding:12px;margin:10px;border-radius:8px;border:1px solid #ccc;">

                            <select name="role"
                                    style="width:97%;padding:12px;margin:10px;border-radius:8px;border:1px solid #ccc;">
                                <option value="USER">USER</option>
                                <option value="ADMIN">ADMIN</option>
                            </select>

                            <button type="submit"
                                    style="width:97%;padding:12px;background:#185a9d;color:white;border:none;border-radius:8px;font-size:16px;cursor:pointer;">
                                Register
                            </button>
                        </form>

                        <p style="margin-top:20px;">
                            Already have an account?
                            <a href="/" style="color:#185a9d;text-decoration:none;font-weight:bold;">Login</a>
                        </p>
                    </div>
                </body>
                </html>
                """;
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String role) {

        if (repository.findByUsername(username).isPresent()) {
            return """
                    <html>
                    <body style="margin:0;font-family:Segoe UI;background:linear-gradient(135deg,#ff9966,#ff5e62);display:flex;justify-content:center;align-items:center;height:100vh;">
                        <div style="background:white;padding:40px;border-radius:20px;width:400px;box-shadow:0 10px 25px rgba(0,0,0,0.25);text-align:center;">
                            <h2 style="color:red;">Username already exists</h2>
                            <p>Please choose another username.</p>
                            <a href="/register" style="color:#ff5e62;font-weight:bold;">Try Again</a>
                        </div>
                    </body>
                    </html>
                    """;
        }

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        repository.save(user);

        return """
                <html>
                <body style="margin:0;font-family:Segoe UI;background:linear-gradient(135deg,#43cea2,#185a9d);display:flex;justify-content:center;align-items:center;height:100vh;">
                    <div style="background:white;padding:40px;border-radius:20px;width:420px;box-shadow:0 10px 25px rgba(0,0,0,0.25);text-align:center;">
                        <h1 style="color:green;">Account Created Successfully</h1>
                        <p>User saved in PostgreSQL with BCrypt encrypted password.</p>
                        <a href="/" style="color:#185a9d;font-weight:bold;">Go to Login</a>
                    </div>
                </body>
                </html>
                """;
    }

    @GetMapping("/public")
    public String publicPage() {
        return """
                <html>
                <body style="margin:0;font-family:Segoe UI;background:linear-gradient(135deg,#56ab2f,#a8e063);display:flex;justify-content:center;align-items:center;height:100vh;">
                    <div style="background:white;padding:40px;border-radius:20px;width:450px;text-align:center;box-shadow:0 10px 25px rgba(0,0,0,0.25);">
                        <h1 style="color:#2e7d32;">Public Endpoint</h1>
                        <p>Anyone can access this page without logging in.</p>
                        <a href="/" style="color:#2e7d32;font-weight:bold;">Go to Login</a>
                    </div>
                </body>
                </html>
                """;
    }

    @GetMapping("/private")
    public String privatePage() {
        return """
                <html>
                <body style="margin:0;font-family:Segoe UI;background:linear-gradient(135deg,#141e30,#243b55);display:flex;justify-content:center;align-items:center;height:100vh;">
                    <div style="background:white;padding:40px;border-radius:20px;width:480px;text-align:center;box-shadow:0 10px 25px rgba(0,0,0,0.25);">
                        <h1 style="color:#243b55;">Private Endpoint</h1>
                        <p>You are authenticated and allowed to access this private page.</p>
                        <form action="/logout" method="post">
                            <button style="padding:12px 25px;background:#243b55;color:white;border:none;border-radius:8px;cursor:pointer;">Logout</button>
                        </form>
                    </div>
                </body>
                </html>
                """;
    }

    private String buildLoginPage(String message) {
        return """
                <html>
                <body style="margin:0;font-family:Segoe UI;background:linear-gradient(135deg,#667eea,#764ba2);display:flex;justify-content:center;align-items:center;height:100vh;">
                    <div style="background:white;padding:40px;border-radius:20px;width:350px;box-shadow:0 10px 25px rgba(0,0,0,0.25);text-align:center;">
                        <h2>Secure Login</h2>

                        %s

                        <form action="/form-login" method="post" autocomplete="off">
                            <input type="text" name="username" placeholder="Username" autocomplete="off" value=""
                                   style="width:90%%;padding:12px;margin:10px;border-radius:8px;border:1px solid #ccc;">

                            <input type="password" name="password" placeholder="Password" autocomplete="new-password" value=""
                                   style="width:90%%;padding:12px;margin:10px;border-radius:8px;border:1px solid #ccc;">

                            <button type="submit"
                                    style="width:95%%;padding:12px;background:#667eea;color:white;border:none;border-radius:8px;font-size:16px;cursor:pointer;">
                                Login
                            </button>
                        </form>

                        <p style="margin-top:20px;">
                            New user?
                            <a href="/register" style="color:#667eea;text-decoration:none;font-weight:bold;">Create account</a>
                        </p>
                    </div>
                </body>
                </html>
                """.formatted(message);
    }
}