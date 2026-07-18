package com.example.lostlink.config;

import com.example.lostlink.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * ============================================================
 * SecurityConfig — Spring Security Configuration
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * This is the CENTER of the entire security system. It configures:
 * 1. Which endpoints are public (no auth needed)
 * 2. Which endpoints require authentication
 * 3. Which roles can access which endpoints
 * 4. How authentication works (stateless JWT, not sessions)
 * 5. CORS settings (which domains can call our API)
 * 6. Password encoding (BCrypt)
 * 
 * @Configuration — Marks this as a Spring configuration class
 * @EnableWebSecurity — Enables Spring Security's web security features
 * @EnableMethodSecurity — Enables @PreAuthorize annotations in controllers
 *   e.g., @PreAuthorize("hasRole('ADMIN')") on a controller method
 * @RequiredArgsConstructor — Lombok: injects JwtAuthenticationFilter
 * ============================================================
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Configures the HTTP security filter chain.
     * 
     * This is the main security configuration method. It defines
     * the security rules for every HTTP request.
     * 
     * @param http the HttpSecurity builder
     * @return the configured SecurityFilterChain bean
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF — we're using JWT tokens, not cookies
            // CSRF protection is only needed for cookie-based session auth
            .csrf(AbstractHttpConfigurer::disable)

            // Configure CORS for cross-origin requests from the React frontend
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // STATELESS session management — no HTTP sessions
            // Each request must carry its own JWT
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // AUTHORIZATION RULES — who can access what
            .authorizeHttpRequests(auth -> auth
                // PUBLIC ENDPOINTS — anyone can access without authentication
                .requestMatchers(
                    "/api/auth/register",
                    "/api/auth/login",
                    "/api/items/search",
                    "/api/items/category/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**",
                    "/actuator/health"
                ).permitAll()

                // ADMIN-ONLY ENDPOINTS
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // SECURITY-STAFF ENDPOINTS
                .requestMatchers(
                    "/api/claims/review/**",
                    "/api/items/status/**",
                    "/api/security/**"
                ).hasAnyRole("SECURITY", "ADMIN")

                // ALL OTHER ENDPOINTS require authentication
                .anyRequest().authenticated()
            )

            // Add our custom JWT filter BEFORE the default username/password filter
            // This ensures JWT validation happens first
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Password encoder bean — BCrypt hashing algorithm.
     * 
     * WHY BCrypt?
     * - Automatically includes a random "salt" — even identical passwords
     *   produce different hashes
     * - Computationally expensive — resistant to brute-force attacks
     * - Industry standard for password hashing
     * 
     * @return BCryptPasswordEncoder instance with strength 10 (default)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager bean — used in the AuthController to
     * authenticate users during login.
     * 
     * @param config Spring's auto-configured AuthenticationConfiguration
     * @return the AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * CORS (Cross-Origin Resource Sharing) configuration.
     * 
     * WHY CORS?
     * Our React frontend runs on http://localhost:3000, but the
     * API runs on http://localhost:8081. Browsers block cross-origin
     * requests by default. CORS headers tell the browser:
     * "Yes, this API is allowed to receive requests from these origins."
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // Pre-flight cache: 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}