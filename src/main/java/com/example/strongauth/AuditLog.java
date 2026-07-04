package com.example.strongauth;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String action;
    private LocalDateTime timestamp;

    public AuditLog() {
    }

    public AuditLog(String username, String action) {
        this.username = username;
        this.action = action;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getAction() {
        return action;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}