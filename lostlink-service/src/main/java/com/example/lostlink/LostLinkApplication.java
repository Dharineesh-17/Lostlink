package com.example.lostlink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * ============================================================
 * LostLinkApplication — Main Entry Point
 * ============================================================
 *
 * @SpringBootApplication — Combines three annotations:
 *   @SpringBootConfiguration: Marks this as a configuration class
 *   @EnableAutoConfiguration: Enables Spring Boot's auto-configuration
 *   @ComponentScan: Scans for @Component, @Service, @Controller, etc.
 *
 * @EnableDiscoveryClient — Registers this service with Eureka Server
 *   so the API Gateway can discover and route to it.
 *
 * @EnableFeignClients — Enables declarative REST clients for
 *   calling other microservices.
 * ============================================================
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class LostLinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(LostLinkApplication.class, args);
    }
}