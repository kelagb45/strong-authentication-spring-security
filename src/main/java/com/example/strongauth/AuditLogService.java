package com.example.strongauth;

import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    private final AuditLogRepository repository;

    public AuditLogService(AuditLogRepository repository) {
        this.repository = repository;
    }

    public void log(String username, String action) {
        repository.save(new AuditLog(username, action));
    }
}