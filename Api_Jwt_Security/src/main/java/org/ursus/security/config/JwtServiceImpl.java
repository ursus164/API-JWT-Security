package org.ursus.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements  JwtService {

    private static final String PublicKey = "c98e8b0e60cbcdca38d874b289e6870935705a3e9339e41964fd8b988d9b495e";

    @Override
    public String extractUsername(String jwt) {
        // we pass Claims::getSubject because in the application, the username (user email) is passed in the SUB
        // field of JWT
        return extractClaim(jwt,Claims::getSubject);
    }

    // extract a single claim that we pass
    public <T> T extractClaim(String jwt, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(jwt);
        return claimResolver.apply(claims);
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

    // generating a JWT token with provided additional information in claims map
    public String generateToken(
            Map<String,Object> extraClaims, // extra claims to pass any additional information like authorities etc...
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .signWith(getPublicSigningKey(), Jwts.SIG.HS256)
                .claims().add(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .and().compact();

    }
    // generating a JWT token without any additional claims
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // Check whether a provided JWT is valid - that means it belongs to the issuing user, and it is not expired
    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        // UserDetails object is passed because we want to check whether the JWT belongs to that user or not
        final String username = extractUsername(jwt);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(jwt);
    }

    // Check if a provided JWT is expired - it can be done via extracting Expiration date from the claims itself.
    private boolean isTokenExpired(String jwt) {
        return extractExpirationDate(jwt).before(new Date());
    }

    private Date extractExpirationDate(String jwt) {
        return extractClaim(jwt, Claims::getExpiration);
    }
}
