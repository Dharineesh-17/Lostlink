package com.example.lostlink.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * ============================================================
 * ResilienceConfig — Circuit Breaker Configuration
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * In microservices, if Service A calls Service B and B goes down,
 * A could hang indefinitely waiting for a response. Circuit Breaker
 * pattern prevents cascading failures:
 * 
 * CLOSED (normal) → After N failures → OPEN (stops calling) → 
 * After timeout → HALF-OPEN (tests if B recovered) → 
 * If success → CLOSED again
 * 
 * @Configuration — Spring configuration class
 * ============================================================
 */
@Configuration
public class ResilienceConfig {

    /**
     * Custom circuit breaker configuration for inter-service calls.
     * 
     * @return CircuitBreakerConfig with tuned thresholds
     */
    @Bean
    public CircuitBreakerConfig circuitBreakerConfig() {
        return CircuitBreakerConfig.custom()
                .failureRateThreshold(50)              // Open circuit if 50% of calls fail
                .waitDurationInOpenState(Duration.ofSeconds(30))  // Wait 30s before retrying
                .slidingWindowSize(10)                 // Evaluate last 10 calls
                .minimumNumberOfCalls(5)               // At least 5 calls before evaluating
                .permittedNumberOfCallsInHalfOpenState(3)  // Try 3 calls in half-open state
                .build();
    }

    /**
     * Registers the circuit breaker with the registry so
     * @CircuitBreaker annotations can reference it.
     */
    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry(CircuitBreakerConfig config) {
        return CircuitBreakerRegistry.of(config);
    }
}