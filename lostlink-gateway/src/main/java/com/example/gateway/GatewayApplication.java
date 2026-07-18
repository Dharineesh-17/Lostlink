package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * ============================================================
 * API Gateway Application
 * ============================================================
 *
 * WHY THIS EXISTS:
 * The API Gateway is the SINGLE entry point for all frontend requests.
 * Instead of the frontend knowing about each microservice's port:
 *   - Frontend calls: http://localhost:8080/api/items
 *   - Gateway routes to: http://localhost:8081/api/items (LostLink Service)
 *
 * Benefits:
 * - CORS handling in one place
 * - Rate limiting
 * - JWT validation (can be added at gateway level)
 * - Load balancing across multiple service instances
 *
 * @EnableDiscoveryClient — Registers with Eureka and can discover other services
 * ============================================================
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}