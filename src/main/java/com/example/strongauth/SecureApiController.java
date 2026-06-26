package com.example.strongauth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecureApiController {

    @GetMapping("/api/secure")
    public String secureApi() {
        return "Access granted. JWT token is valid. This is a secured REST API.";
    }
}