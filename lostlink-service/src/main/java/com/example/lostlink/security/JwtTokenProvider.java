package com.example.lostlink.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * ============================================================
 * JwtTokenProvider — JWT Token Creation and Validation
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * JWT (JSON Web Token) is a stateless authentication mechanism.
 * After login, the server creates a JWT containing the user's
 * email and role, signs it with a secret key, and sends it
 * to the client. The client includes this token in every
 * subsequent request's Authorization header.
 * 
 * The server validates the token on each request — no session
 * storage needed. This makes JWT ideal for microservices
 * because any service can verify the token independently.
 * 
 * @Component — Spring creates and manages this bean automatically.
 *   It's a "utility component" with no state of its own.
 * 
 * HOW JWT WORKS:
 * 1. Header: {"alg": "HS256", "typ": "JWT"}
 * 2. Payload: {"sub": "user@email.com", "role": "STUDENT", "iat": ..., "exp": ...}
 * 3. Signature: HMAC-SHA256(base64(header) + "." + base64(payload), secret)
 * ============================================================
 */
@Component
public class JwtTokenProvider {

    /**
     * jwt.secret is loaded from application.yml.
     * @Value injects Spring property values into fields.
     * Should be at least 256 bits (32 chars) for HS256.
     */
    @Value("${jwt.secret}")
    private String jwtSecret;

    /** Token validity in milliseconds (24 hours by default) */
    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    /**
     * Generates a JWT token after successful authentication.
     * 
     * @param authentication the authenticated principal from Spring Security
     * @return signed JWT token string
     * 
     * BUILD PROCESS:
     * 1. Get username (email) from the authentication object
     * 2. Set issued-at time (now)
     * 3. Set expiration time (now + jwtExpiration)
     * 4. Sign with the secret key using HS256 algorithm
     */
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .subject(userDetails.getUsername())     // "sub" claim = email
                .issuedAt(now)                           // "iat" claim
                .expiration(expiryDate)                  // "exp" claim
                .signWith(getSigningKey())               // Sign with secret key
                .compact();                              // Build the JWT string
    }

    /**
     * Generates a JWT token from a username and role (for custom scenarios).
     */
    public String generateToken(String username, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extracts the username (email) from a JWT token.
     * 
     * @param token the JWT string
     * @return the username stored in the "sub" claim
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())     // Verify signature first
                .build()
                .parseSignedClaims(token)        // Parse the token
                .getPayload();                   // Get the claims/body

        return claims.getSubject();
    }

    /**
     * Extracts the role from a JWT token.
     */
    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("role", String.class);
    }

    /**
     * Validates a JWT token.
     * 
     * @param token the JWT string
     * @return true if the token is valid (properly signed and not expired)
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (SecurityException ex) {
            // Token signature doesn't match
        } catch (MalformedJwtException ex) {
            // Token is malformed (not a valid JWT structure)
        } catch (ExpiredJwtException ex) {
            // Token has expired
        } catch (UnsupportedJwtException ex) {
            // Token uses an unsupported algorithm
        } catch (IllegalArgumentException ex) {
            // Token is empty or null
        }
        return false;
    }

    /**
     * Creates the signing key from the base64-encoded secret.
     * 
     * The secret is stored as a Base64 string in application.yml.
     * Keys.hmacShaKeyFor() decodes it and creates a SecretKey
     * suitable for HMAC-SHA256 signing.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}