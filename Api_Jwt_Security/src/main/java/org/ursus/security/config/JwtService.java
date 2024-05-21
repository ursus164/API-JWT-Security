package org.ursus.security.config;

import org.springframework.stereotype.Service;

@Service
public interface JwtService {
    String extractUsername(String jwt);
}
