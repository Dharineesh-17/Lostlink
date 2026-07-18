package com.example.lostlink.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * ============================================================
 * JwtAuthenticationFilter — Intercepts Every HTTP Request
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * In a stateless JWT system, the server doesn't maintain sessions.
 * Instead, EVERY request must carry its JWT in the Authorization header.
 * This filter runs ONCE per request (extends OncePerRequestFilter)
 * and does the following:
 * 
 * 1. Extract the JWT from the "Authorization: Bearer <token>" header
 * 2. Validate the token (signature + expiration)
 * 3. Load the user from the database using the email in the token
 * 4. Set the authenticated user in Spring Security's SecurityContext
 * 
 * After this filter runs, Spring Security knows WHO the user is
 * and can enforce role-based access control.
 * 
 * @Component — Auto-detected by Spring component scanning
 * @RequiredArgsConstructor — Lombok: generates constructor for final fields (jwtTokenProvider, userDetailsService)
 * @Slf4j — Lombok: generates a logger field named "log"
 * 
 * WHERE IN THE FILTER CHAIN?
 * This filter is added BEFORE the username/password filter in
 * SecurityConfig. It runs first, and if a valid JWT is found,
 * it sets the authentication — so the UsernamePasswordAuthenticationFilter
 * is effectively bypassed for already-authenticated requests.
 * ============================================================
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    /**
     * The core filter logic — runs for every HTTP request.
     * 
     * @param request     the incoming HTTP request
     * @param response    the outgoing HTTP response
     * @param filterChain  the rest of the filter chain (must call chain.doFilter() to continue)
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            // Step 1: Extract JWT from the Authorization header
            String jwt = extractJwtFromRequest(request);

            // Step 2: If a token exists and is valid, authenticate the user
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                String username = jwtTokenProvider.getUsernameFromToken(jwt);

                // Step 3: Load user details from the database
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Step 4: Create an authentication token and set it in SecurityContext
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,           // No credentials needed (already verified by JWT)
                                userDetails.getAuthorities()  // User's roles/permissions
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // This tells Spring Security: "this request is authenticated as this user"
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("Set authentication for user: {}", username);
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }

        // Continue to the next filter in the chain (CRITICAL — don't forget this!)
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the Authorization header.
     * 
     * Expected format: "Bearer eyJhbGciOiJIUzI1NiJ9..."
     * 
     * @param request the HTTP request
     * @return the token string (without "Bearer " prefix), or null if not found
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove "Bearer " prefix (7 characters)
        }
        return null;
    }
}