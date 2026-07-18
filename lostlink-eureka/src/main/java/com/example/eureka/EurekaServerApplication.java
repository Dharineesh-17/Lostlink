package com.example.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * ============================================================
 * Eureka Server Application
 * ============================================================
 *
 * WHY THIS EXISTS:
 * In a microservices architecture, services need to find each other
 * without hardcoding IP addresses and ports. Eureka is a service
 * registry that acts like a phone book:
 *
 * 1. Each service registers itself: "I'm LostLink Service, I'm at 192.168.1.5:8081"
 * 2. The API Gateway asks Eureka: "Where is LostLink Service?"
 * 3. Eureka responds: "It's at 192.168.1.5:8081"
 * 4. Gateway forwards the request there
 *
 * @EnableEurekaServer — Turns this app into a Eureka Server
 *   (normally it would be a Eureka Client)
 *
 * Access the Eureka dashboard at: http://localhost:8761
 * ============================================================
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}