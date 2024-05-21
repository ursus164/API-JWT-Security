package org.ursus.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtServiceImpl implements  JwtService {

    private static final String PublicKey = "c98e8b0e60cbcdca38d874b289e6870935705a3e9339e41964fd8b988d9b495e";

    @Override
    public String extractUsername(String jwt) {
        return "";
    }

    // extract all the claims from the JWT
    private Claims extractAllClaims(String jwt) {
        return Jwts
                .parser()
                .verifyWith(getPublicSigningKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    private SecretKey getPublicSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(PublicKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
