package com.example.strongauth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScopeController {

    @GetMapping("/scope/email")
    public String emailScope() {
        return "Access granted: You have the email scope.";
    }

    @GetMapping("/scope/profile")
    public String profileScope() {
        return "Access granted: You have the profile scope.";
    }
}