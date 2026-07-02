package com.example.strongauth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2TestController {

    @GetMapping("/oauth2/secure")
    public String secure() {
        return "OAuth2 Resource Server works successfully!";
    }
}