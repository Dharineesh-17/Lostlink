package com.example.lostlink.security;

import com.example.lostlink.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * ============================================================
 * CustomUserDetailsService — Bridge Between User Table and Spring Security
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * Spring Security doesn't know about our User entity. It works
 * with a "UserDetails" interface. This service loads our custom
 * User entity from the database and converts it into a Spring
 * Security UserDetails object that the framework can use for
 * authentication and authorization.
 * 
 * @Service — Marks this as a Spring-managed service bean
 * @Transactional — Opens a database transaction for the query
 * 
 * THE FLOW:
 * 1. JwtAuthenticationFilter calls loadUserByUsername(email)
 * 2. This method queries the database via UserRepository
 * 3. Converts our User entity into Spring Security's User object
 * 4. The User object contains:
 *    - username (email)
 *    - password (hashed, for potential re-authentication)
 *    - authorities (roles converted to "ROLE_STUDENT" etc.)
 * ============================================================
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads a user by their email address.
     * Called by JwtAuthenticationFilter on every authenticated request.
     * Also called by Spring Security's DaoAuthenticationProvider during login.
     * 
     * @param email the user's email address (used as username)
     * @return UserDetails object containing user info and authorities
     * @throws UsernameNotFoundException if no user exists with this email
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Loading user by email: {}", email);

        com.example.lostlink.entity.User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with email: " + email
                ));

        // Convert our custom role enum to Spring Security's GrantedAuthority
        // The "ROLE_" prefix is REQUIRED by Spring Security's hasRole() method
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );

        // Build Spring Security's User object (not our custom entity)
        return new User(
                user.getEmail(),        // username = email
                user.getPassword(),     // hashed password
                authorities             // roles
        );
    }
}